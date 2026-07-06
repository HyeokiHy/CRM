package com.example.b2bcrm.deal;

import java.util.Arrays;

public enum DealStage {
    REGISTRATION("Registration", 10),
    ACCESS("Access", 45),
    GO_NO_GO("Go - No Go", 25),
    AWARD("Award", 75),
    CLOSED("Closed", 100);

    private final String label;
    private final int defaultProbability;

    DealStage(String label, int defaultProbability) {
        this.label = label;
        this.defaultProbability = defaultProbability;
    }

    public String getLabel() {
        return label;
    }

    public int getDefaultProbability() {
        return defaultProbability;
    }

    public DealStage next() {
        int nextIndex = ordinal() + 1;
        DealStage[] stages = values();
        return nextIndex >= stages.length ? this : stages[nextIndex];
    }

    public DealStage previous() {
        int previousIndex = ordinal() - 1;
        return previousIndex < 0 ? this : values()[previousIndex];
    }

    public static DealStage fromLabel(String label) {
        return Arrays.stream(values())
            .filter(stage -> stage.label.equalsIgnoreCase(label) || stage.name().equalsIgnoreCase(label))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown deal stage: " + label));
    }
}
