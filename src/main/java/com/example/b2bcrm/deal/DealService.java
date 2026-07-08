package com.example.b2bcrm.deal;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import com.example.b2bcrm.deal.dto.DealCreateRequest;
import com.example.b2bcrm.deal.dto.DealMoveRequest;
import com.example.b2bcrm.deal.dto.DealResponse;
import com.example.b2bcrm.deal.dto.DealUpdateRequest;
import com.example.b2bcrm.deal.mapper.DealMapper;
import com.example.b2bcrm.user.AppUser;
import com.example.b2bcrm.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DealService {

    private final DealRepository dealRepository;
    private final UserService userService;
    private final DealMapper dealMapper;

    public DealService(DealRepository dealRepository, UserService userService, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.userService = userService;
        this.dealMapper = dealMapper;
    }

    public List<DealResponse> findDeals(String search, String stage, String priority) {
        String normalizedSearch = normalize(search);
        String normalizedStage = normalize(stage);
        String normalizedPriority = normalize(priority);

        return dealRepository.findAll().stream()
            .filter(deal -> matchesSearch(deal, normalizedSearch))
            .filter(deal -> matchesStage(deal, normalizedStage))
            .filter(deal -> matchesPriority(deal, normalizedPriority))
            .sorted(Comparator.comparing(Deal::getCloseDate))
            .map(dealMapper::toResponse)
            .collect(Collectors.toList());
    }

    public DealResponse findDeal(Long id) {
        return dealMapper.toResponse(getDeal(id));
    }

    public DealResponse createDeal(DealCreateRequest request) {
        Deal deal = dealMapper.toEntity(request);
        return dealMapper.toResponse(dealRepository.save(deal));
    }

    public DealResponse updateDeal(Long id, DealUpdateRequest request) {
        Deal deal = getDeal(id);
        dealMapper.updateEntity(deal, request);
        return dealMapper.toResponse(dealRepository.save(deal));
    }

    public DealResponse moveDeal(Long id, DealMoveRequest request) {
        Deal deal = getDeal(id);
        AppUser actor = userService.authenticate(request.getUsername(), request.getPassword());
        DealStage nextStage;

        if ("next".equalsIgnoreCase(request.getDirection())) {
            nextStage = deal.getStage().next();
            validateForwardMove(deal, nextStage, actor);
        } else if ("back".equalsIgnoreCase(request.getDirection()) || "previous".equalsIgnoreCase(request.getDirection())) {
            nextStage = deal.getStage().previous();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Direction must be next or back.");
        }

        deal.setStage(nextStage);
        deal.setProbability(nextStage.getDefaultProbability());
        return dealMapper.toResponse(dealRepository.save(deal));
    }

    private void validateForwardMove(Deal deal, DealStage nextStage, AppUser actor) {
        if (deal.getStage() == nextStage) {
            return;
        }

        if (deal.getStage() == DealStage.REGISTRATION && nextStage == DealStage.ACCESS) {
            requireCustomerAndBudget(deal);
            return;
        }

        if (deal.getStage() == DealStage.ACCESS && nextStage == DealStage.GO_NO_GO) {
            requireOpportunityLocationAndItems(deal);
            return;
        }

        if (deal.getStage() == DealStage.GO_NO_GO && nextStage == DealStage.AWARD) {
            requireAdmin(actor);
            return;
        }

        if (deal.getStage() == DealStage.AWARD && nextStage == DealStage.CLOSED) {
            requireOwnerOrAdmin(deal, actor);
        }
    }

    private void requireCustomerAndBudget(Deal deal) {
        if (isBlank(deal.getCompany()) || isBlank(deal.getContact()) || deal.getValue().signum() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Registration to Access requires customer information and a budget value."
            );
        }
    }

    private void requireOpportunityLocationAndItems(Deal deal) {
        if (isBlank(deal.getOpportunityLocation()) || isBlank(deal.getExpectedItems())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Access to Go - No Go requires opportunity location and expected items or materials."
            );
        }
    }

    private void requireAdmin(AppUser actor) {
        if (!userService.isAdmin(actor)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Only an admin can approve a Go - No Go deal for Award."
            );
        }
    }

    private void requireOwnerOrAdmin(Deal deal, AppUser actor) {
        if (userService.isAdmin(actor) || deal.getOwner().equalsIgnoreCase(actor.getUsername())) {
            return;
        }

        throw new ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "Only the deal owner or an admin can close an Award deal."
        );
    }

    public void deleteDeal(Long id) {
        Deal deal = getDeal(id);
        dealRepository.delete(deal);
    }

    private Deal getDeal(Long id) {
        return dealRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal not found."));
    }

    private boolean matchesSearch(Deal deal, String search) {
        if (search.isEmpty()) {
            return true;
        }

        String searchable = String.join(" ",
            deal.getCompany(),
            deal.getContact(),
            deal.getOwner(),
            deal.getStage().getLabel(),
            deal.getPriority().getLabel(),
            deal.getNextAction(),
            nullToEmpty(deal.getOpportunityLocation()),
            nullToEmpty(deal.getExpectedItems())
        ).toLowerCase(Locale.ROOT);

        return searchable.contains(search);
    }

    private boolean matchesStage(Deal deal, String stage) {
        return stage.isEmpty()
            || "all".equals(stage)
            || deal.getStage().name().toLowerCase(Locale.ROOT).equals(stage)
            || deal.getStage().getLabel().toLowerCase(Locale.ROOT).equals(stage);
    }

    private boolean matchesPriority(Deal deal, String priority) {
        return priority.isEmpty()
            || "all".equals(priority)
            || deal.getPriority().name().toLowerCase(Locale.ROOT).equals(priority)
            || deal.getPriority().getLabel().toLowerCase(Locale.ROOT).equals(priority);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
