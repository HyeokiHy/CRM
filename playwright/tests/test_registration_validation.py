import pytest
from playwright.sync_api import Page

from data.opportunity_data import newOpportunity
from pages.crm_board_page import CrmBoardPage
from pages.opportunity_dialog import OpportunityDialog


@pytest.mark.parametrize("requiredLabel", ["Company", "Contact", "Owner", "Next action"])
def test_registration_blocks_submission_when_required_field_is_empty(
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
    requiredLabel: str,
) -> None:
    opportunity = newOpportunity()
    crmBoardPage.newOpportunityButton().click()
    opportunityDialog.fillOpportunity(opportunity.formValues())
    opportunityDialog.field(requiredLabel).fill("")

    opportunityDialog.submit()

    opportunityDialog.expectOpen()
    opportunityDialog.expectRequiredFieldError(requiredLabel)
    crmBoardPage.expectOpportunityNotInStage("Registration", opportunity.company)


def test_registration_creates_valid_opportunity_and_shows_it_on_board(
    page: Page,
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    opportunity = newOpportunity()
    crmBoardPage.newOpportunityButton().click()
    opportunityDialog.fillOpportunity(opportunity.formValues())

    with page.expect_response(
        lambda response: response.url.endswith("/api/deals")
        and response.request.method == "POST",
    ) as responseInfo:
        opportunityDialog.submit()

    response = responseInfo.value
    assert response.status == 201
    assert response.json()["stageCode"] == "REGISTRATION"
    assert response.json()["company"] == opportunity.company
    opportunityDialog.expectClosed()
    crmBoardPage.expectOpportunityInStage("Registration", opportunity.company)
