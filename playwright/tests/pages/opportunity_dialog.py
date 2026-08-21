from playwright.sync_api import Locator, Page, expect


class OpportunityDialog:
    def __init__(self, page: Page) -> None:
        self.dialog = page.get_by_role("dialog")

    def expectOpen(self) -> None:
        expect(self.dialog).to_be_visible()

    def expectClosed(self) -> None:
        expect(self.dialog).not_to_be_visible()

    def field(self, label: str) -> Locator:
        return self.dialog.get_by_label(label, exact=True)

    def fillOpportunity(self, values: dict[str, str]) -> None:
        fieldLabels = {
            "company": "Company", "contact": "Contact", "owner": "Owner",
            "value": "Value", "probability": "Probability",
            "closeDate": "Close date", "opportunityLocation": "Opportunity location",
            "expectedItems": "Items or materials", "nextAction": "Next action",
        }
        for fieldName, label in fieldLabels.items():
            self.field(label).fill(values[fieldName])
        self.field("Stage").select_option(values["stage"])
        self.field("Priority").select_option(values["priority"])

    def submit(self) -> None:
        self.dialog.get_by_role("button", name="Save Deal", exact=True).click()

    def expectRequiredFieldError(self, label: str) -> None:
        inputField = self.field(label)
        expect(inputField).to_be_focused()
        assert inputField.evaluate("element => element.validity.valueMissing") is True
        assert inputField.evaluate("element => element.validationMessage")
