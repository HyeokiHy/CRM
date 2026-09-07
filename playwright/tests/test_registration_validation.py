import pytest
from playwright.sync_api import Page

from data.deal_test_data import newDeal
from pages.crm_board_page import CrmBoardPage
from pages.deal_dialog import DealDialog


@pytest.mark.parametrize("requiredLabel", ["Company", "Contact", "Owner", "Next action"])
def test_registration_blocks_submission_when_required_field_is_empty(
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
    requiredLabel: str,
) -> None:
    deal = newDeal()
    crmBoardPage.newDealButton().click()
    dealDialog.fillDeal(deal.formValues())
    dealDialog.field(requiredLabel).fill("")

    dealDialog.submit()

    dealDialog.expectOpen()
    dealDialog.expectRequiredFieldError(requiredLabel)
    crmBoardPage.expectDealNotInStage("Registration", deal.company)


def test_registration_creates_valid_deal_and_shows_it_on_board(
    page: Page,
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
) -> None:
    deal = newDeal()
    crmBoardPage.newDealButton().click()
    dealDialog.fillDeal(deal.formValues())

    with page.expect_response(
        lambda response: response.url.endswith("/api/deals")
        and response.request.method == "POST",
    ) as responseInfo:
        dealDialog.submit()

    response = responseInfo.value
    assert response.status == 201
    assert response.json()["stageCode"] == "REGISTRATION"
    assert response.json()["company"] == deal.company
    dealDialog.expectClosed()
    crmBoardPage.expectDealInStage("Registration", deal.company)
