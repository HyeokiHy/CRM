from playwright.sync_api import Locator, Page, expect


class CrmBoardPage:
    def __init__(self, page: Page, baseUrl: str) -> None:
        self.page = page
        self.baseUrl = baseUrl

    def open(self) -> None:
        self.page.goto(self.baseUrl, wait_until="domcontentloaded")
        expect(self.newOpportunityButton()).to_be_visible()

    def newOpportunityButton(self) -> Locator:
        return self.page.get_by_role("button", name="New Deal", exact=True)

    def stageColumn(self, stageName: str) -> Locator:
        return self.page.locator(".stage-column").filter(
            has=self.page.get_by_role("heading", name=stageName, exact=True),
        )

    def dealCard(self, stageName: str, company: str) -> Locator:
        return self.stageColumn(stageName).locator(".deal-card").filter(
            has=self.page.get_by_role("button", name=company, exact=False),
        )

    def moveOpportunity(self, stageName: str, company: str, direction: str) -> None:
        buttonName = "Next" if direction == "next" else "Prev"
        self.dealCard(stageName, company).get_by_role(
            "button", name=buttonName, exact=True,
        ).click()

    def setCurrentUser(self, username: str, password: str) -> None:
        self.page.get_by_label("User", exact=True).fill(username)
        self.page.get_by_label("Password", exact=True).fill(password)

    def expectOpportunityInStage(self, stageName: str, company: str) -> None:
        expect(self.dealCard(stageName, company)).to_have_count(1)

    def expectOpportunityNotInStage(self, stageName: str, company: str) -> None:
        expect(self.dealCard(stageName, company)).to_have_count(0)
