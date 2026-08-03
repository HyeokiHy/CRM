from playwright.sync_api import Locator, Page, expect


class OpportunityDialog:
    def __init__(self, page: Page) -> None:
        self.page = page
        self.dialog = page.get_by_role("dialog")

    def expectOpen(self) -> None:
        expect(self.dialog).to_be_visible()

    def expectClosed(self) -> None:
        expect(self.dialog).not_to_be_visible()

    def companyInput(self) -> Locator:
        return self.dialog.get_by_label("Company", exact=True)

    def ownerInput(self) -> Locator:
        return self.dialog.get_by_label("Owner", exact=True)

    def stageSelect(self) -> Locator:
        return self.dialog.get_by_label("Stage", exact=True)

    def saveButton(self) -> Locator:
        return self.dialog.get_by_role(
            "button",
            name="Save Deal",
            exact=True,
        )

    def cancelButton(self) -> Locator:
        return self.dialog.get_by_role(
            "button",
            name="Cancel",
            exact=True,
        )

    def closeButton(self) -> Locator:
        return self.dialog.get_by_role(
            "button",
            name="Close",
            exact=True,
        )
