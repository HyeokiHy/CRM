from dataclasses import asdict, dataclass
from uuid import uuid4


@dataclass(frozen=True)
class OpportunityData:
    company: str
    contact: str = "QA Contact"
    owner: str = "J. Kim"
    stage: str = "REGISTRATION"
    value: int = 100_000
    probability: int = 10
    priority: str = "MEDIUM"
    closeDate: str = "2026-12-31"
    opportunityLocation: str = "Seoul Office"
    expectedItems: str = "Automation equipment"
    nextAction: str = "Review requirements"

    def formValues(self) -> dict[str, str]:
        return {key: str(value) for key, value in asdict(self).items()}

    def apiPayload(self) -> dict[str, object]:
        return asdict(self)


def newOpportunity(**overrides: object) -> OpportunityData:
    return OpportunityData(company=f"QA E2E {uuid4().hex[:8]}", **overrides)
