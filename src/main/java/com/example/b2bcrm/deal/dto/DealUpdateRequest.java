package com.example.b2bcrm.deal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.b2bcrm.deal.DealStage;
import com.example.b2bcrm.deal.Priority;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DealUpdateRequest {

    @NotBlank
    @Size(max = 80)
    private String company;

    @NotBlank
    @Size(max = 80)
    private String contact;

    @NotBlank
    @Size(max = 60)
    private String owner;

    @NotNull
    private DealStage stage;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal value;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer probability;

    @NotNull
    private Priority priority;

    @NotNull
    private LocalDate closeDate;

    @NotBlank
    @Size(max = 180)
    private String nextAction;

    @Size(max = 120)
    private String opportunityLocation;

    @Size(max = 180)
    private String expectedItems;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public DealStage getStage() {
        return stage;
    }

    public void setStage(DealStage stage) {
        this.stage = stage;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public String getOpportunityLocation() {
        return opportunityLocation;
    }

    public void setOpportunityLocation(String opportunityLocation) {
        this.opportunityLocation = opportunityLocation;
    }

    public String getExpectedItems() {
        return expectedItems;
    }

    public void setExpectedItems(String expectedItems) {
        this.expectedItems = expectedItems;
    }
}
