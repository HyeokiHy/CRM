from playwright.sync_api import Locator, Page


class CrmBoardPage:
    def __init__(
        self,
        page: Page,
        baseUrl: str,
    ) -> None:
        self.page = page
        self.baseUrl = baseUrl

    def open(self) -> None:
        self.page.goto(self.baseUrl)

    def newOpportunityButton(self) -> Locator:
        return self.page.get_by_role(
            "button",
            name="New Deal",
            exact=True,
        )

    def stageColumn(self, stageName: str) -> Locator:
        return self.page.locator(".stage-column").filter(
            has=self.page.get_by_role(
                "heading",
                name=stageName,
                exact=True,
            ),
        )

    def opportunityCards(self, stageName: str) -> Locator:
        return self.stageColumn(stageName).locator(".deal-card")
