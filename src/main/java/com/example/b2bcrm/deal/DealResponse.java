package com.example.b2bcrm.deal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DealResponse {

    private final Long id;
    private final String company;
    private final String contact;
    private final String owner;
    private final String stage;
    private final String stageCode;
    private final BigDecimal value;
    private final Integer probability;
    private final String priority;
    private final String priorityCode;
    private final LocalDate closeDate;
    private final String nextAction;
    private final String opportunityLocation;
    private final String expectedItems;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public DealResponse(Deal deal) {
        id = deal.getId();
        company = deal.getCompany();
        contact = deal.getContact();
        owner = deal.getOwner();
        stage = deal.getStage().getLabel();
        stageCode = deal.getStage().name();
        value = deal.getValue();
        probability = deal.getProbability();
        priority = deal.getPriority().getLabel();
        priorityCode = deal.getPriority().name();
        closeDate = deal.getCloseDate();
        nextAction = deal.getNextAction();
        opportunityLocation = deal.getOpportunityLocation();
        expectedItems = deal.getExpectedItems();
        createdAt = deal.getCreatedAt();
        updatedAt = deal.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getContact() {
        return contact;
    }

    public String getOwner() {
        return owner;
    }

    public String getStage() {
        return stage;
    }

    public String getStageCode() {
        return stageCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Integer getProbability() {
        return probability;
    }

    public String getPriority() {
        return priority;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public String getNextAction() {
        return nextAction;
    }

    public String getOpportunityLocation() {
        return opportunityLocation;
    }

    public String getExpectedItems() {
        return expectedItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
