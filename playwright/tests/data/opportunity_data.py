from dataclasses import dataclass


@dataclass(frozen=True)
class OpportunityStage:
    displayName: str
    stageCode: str
    testId: str


OPPORTUNITY_STAGES = (
    OpportunityStage(
        displayName="Registration",
        stageCode="REGISTRATION",
        testId="registration",
    ),
    OpportunityStage(
        displayName="Access",
        stageCode="ACCESS",
        testId="access",
    ),
    OpportunityStage(
        displayName="Go - No Go",
        stageCode="GO_NO_GO",
        testId="go-no-go",
    ),
    OpportunityStage(
        displayName="Award",
        stageCode="AWARD",
        testId="award",
    ),
    OpportunityStage(
        displayName="Closed",
        stageCode="CLOSED",
        testId="closed",
    ),
)
