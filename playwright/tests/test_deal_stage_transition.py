import pytest
from playwright.sync_api import Page, Response

from data.deal_test_data import DealTestData, newDeal
from pages.crm_board_page import CrmBoardPage
from pages.deal_dialog import DealDialog


STAGE_NAMES = {
    "REGISTRATION": "Registration",
    "ACCESS": "Access",
    "GO_NO_GO": "Go - No Go",
    "AWARD": "Award",
}


def createDeal(
    page: Page,
    board: CrmBoardPage,
    dialog: DealDialog,
    deal: DealTestData,
) -> None:
    board.newDealButton().click()
    dialog.fillDeal(deal.formValues())
    with page.expect_response(
        lambda response: response.url.endswith("/api/deals")
        and response.request.method == "POST",
    ) as responseInfo:
        dialog.submit()
    assert responseInfo.value.status == 201
    dialog.expectClosed()
    board.expectDealInStage(STAGE_NAMES[deal.stage], deal.company)


def moveNext(page: Page, board: CrmBoardPage, stageName: str, company: str) -> Response:
    with page.expect_response(
        lambda response: "/move" in response.url
        and response.request.method == "PATCH",
    ) as responseInfo:
        board.moveDeal(stageName, company, "next")
    return responseInfo.value


def test_registration_to_access_rejects_zero_budget(
    page: Page,
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
) -> None:
    deal = newDeal(value=0)
    createDeal(page, crmBoardPage, dealDialog, deal)

    response = moveNext(page, crmBoardPage, "Registration", deal.company)

    assert response.status == 400
    assert response.json()["message"] == (
        "Registration to Access requires customer information and a budget value."
    )
    crmBoardPage.expectDealInStage("Registration", deal.company)
    crmBoardPage.expectDealNotInStage("Access", deal.company)


@pytest.mark.parametrize("missingField", ["opportunityLocation", "expectedItems"])
def test_access_to_go_no_go_requires_location_and_expected_items(
    page: Page,
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
    missingField: str,
) -> None:
    deal = newDeal(
        stage="ACCESS",
        probability=45,
        **{missingField: ""},
    )
    createDeal(page, crmBoardPage, dealDialog, deal)

    response = moveNext(page, crmBoardPage, "Access", deal.company)

    assert response.status == 400
    assert response.json()["message"] == (
        "Access to Go - No Go requires opportunity location and expected items or materials."
    )
    crmBoardPage.expectDealInStage("Access", deal.company)
    crmBoardPage.expectDealNotInStage("Go - No Go", deal.company)


def test_go_no_go_to_award_requires_admin_and_allows_admin_approval(
    page: Page,
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
) -> None:
    deal = newDeal(stage="GO_NO_GO", probability=25)
    createDeal(page, crmBoardPage, dealDialog, deal)
    crmBoardPage.setCurrentUser("J. Kim", "password")

    rejected = moveNext(page, crmBoardPage, "Go - No Go", deal.company)

    assert rejected.status == 403
    assert rejected.json()["message"] == (
        "Only an admin can approve a Go - No Go deal for Award."
    )
    crmBoardPage.expectDealInStage("Go - No Go", deal.company)

    crmBoardPage.setCurrentUser("Admin", "Admin")
    approved = moveNext(page, crmBoardPage, "Go - No Go", deal.company)

    assert approved.status == 200
    assert approved.json()["stageCode"] == "AWARD"
    assert approved.json()["probability"] == 75
    crmBoardPage.expectDealInStage("Award", deal.company)


def test_award_to_closed_requires_owner_or_admin(
    page: Page,
    crmBoardPage: CrmBoardPage,
    dealDialog: DealDialog,
) -> None:
    deal = newDeal(stage="AWARD", probability=75, owner="J. Kim")
    createDeal(page, crmBoardPage, dealDialog, deal)
    crmBoardPage.setCurrentUser("S. Lee", "password")

    rejected = moveNext(page, crmBoardPage, "Award", deal.company)

    assert rejected.status == 403
    assert rejected.json()["message"] == (
        "Only the deal owner or an admin can close an Award deal."
    )
    crmBoardPage.expectDealInStage("Award", deal.company)

    crmBoardPage.setCurrentUser("J. Kim", "password")
    closed = moveNext(page, crmBoardPage, "Award", deal.company)

    assert closed.status == 200
    assert closed.json()["stageCode"] == "CLOSED"
    assert closed.json()["probability"] == 100
    crmBoardPage.expectDealInStage("Closed", deal.company)
