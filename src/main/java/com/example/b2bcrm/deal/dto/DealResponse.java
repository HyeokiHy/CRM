package com.example.b2bcrm.deal.dto;

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

    public DealResponse(
        Long id,
        String company,
        String contact,
        String owner,
        String stage,
        String stageCode,
        BigDecimal value,
        Integer probability,
        String priority,
        String priorityCode,
        LocalDate closeDate,
        String nextAction,
        String opportunityLocation,
        String expectedItems,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.company = company;
        this.contact = contact;
        this.owner = owner;
        this.stage = stage;
        this.stageCode = stageCode;
        this.value = value;
        this.probability = probability;
        this.priority = priority;
        this.priorityCode = priorityCode;
        this.closeDate = closeDate;
        this.nextAction = nextAction;
        this.opportunityLocation = opportunityLocation;
        this.expectedItems = expectedItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
