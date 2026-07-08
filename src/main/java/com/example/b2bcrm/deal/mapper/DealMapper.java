package com.example.b2bcrm.deal.mapper;

import com.example.b2bcrm.deal.Deal;
import com.example.b2bcrm.deal.dto.DealCreateRequest;
import com.example.b2bcrm.deal.dto.DealResponse;
import com.example.b2bcrm.deal.dto.DealUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class DealMapper {

    public Deal toEntity(DealCreateRequest request) {
        Deal deal = new Deal();
        applyCreateRequest(deal, request);
        return deal;
    }

    public void updateEntity(Deal deal, DealUpdateRequest request) {
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

    public DealResponse toResponse(Deal deal) {
        return new DealResponse(
            deal.getId(),
            deal.getCompany(),
            deal.getContact(),
            deal.getOwner(),
            deal.getStage().getLabel(),
            deal.getStage().name(),
            deal.getValue(),
            deal.getProbability(),
            deal.getPriority().getLabel(),
            deal.getPriority().name(),
            deal.getCloseDate(),
            deal.getNextAction(),
            deal.getOpportunityLocation(),
            deal.getExpectedItems(),
            deal.getCreatedAt(),
            deal.getUpdatedAt()
        );
    }

    private void applyCreateRequest(Deal deal, DealCreateRequest request) {
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

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
