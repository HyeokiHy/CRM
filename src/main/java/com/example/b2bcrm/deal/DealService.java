package com.example.b2bcrm.deal;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import com.example.b2bcrm.user.AppUser;
import com.example.b2bcrm.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DealService {

    private final DealRepository dealRepository;
    private final UserService userService;

    public DealService(DealRepository dealRepository, UserService userService) {
        this.dealRepository = dealRepository;
        this.userService = userService;
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
            .map(DealResponse::new)
            .collect(Collectors.toList());
    }

    public DealResponse findDeal(Long id) {
        return new DealResponse(getDeal(id));
    }

    public DealResponse createDeal(DealRequest request) {
        Deal deal = new Deal();
        applyRequest(deal, request);
        return new DealResponse(dealRepository.save(deal));
    }

    public DealResponse updateDeal(Long id, DealRequest request) {
        Deal deal = getDeal(id);
        applyRequest(deal, request);
        return new DealResponse(dealRepository.save(deal));
    }

    public DealResponse moveDeal(Long id, MoveDealRequest request) {
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
        return new DealResponse(dealRepository.save(deal));
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
            requireAdmin(actor, "Only an admin can approve a Go - No Go deal for Award.");
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

    private void requireAdmin(AppUser actor, String message) {
        if (!userService.isAdmin(actor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
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

    private void applyRequest(Deal deal, DealRequest request) {
        deal.setCompany(request.getCompany().trim());
        deal.setContact(request.getContact().trim());
        deal.setOwner(request.getOwner().trim());
        deal.setStage(request.getStage());
        deal.setValue(request.getValue());
        deal.setProbability(request.getProbability());
        deal.setPriority(request.getPriority());
        deal.setCloseDate(request.getCloseDate());
        deal.setNextAction(request.getNextAction().trim());
        deal.setOpportunityLocation(trimToNull(request.getOpportunityLocation()));
        deal.setExpectedItems(trimToNull(request.getExpectedItems()));
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

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
