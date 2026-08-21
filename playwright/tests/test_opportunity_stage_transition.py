import pytest
from playwright.sync_api import Page, Response

from data.opportunity_data import OpportunityData, newOpportunity
from pages.crm_board_page import CrmBoardPage
from pages.opportunity_dialog import OpportunityDialog


STAGE_NAMES = {
    "REGISTRATION": "Registration",
    "ACCESS": "Access",
    "GO_NO_GO": "Go - No Go",
    "AWARD": "Award",
}


def createOpportunity(
    page: Page,
    board: CrmBoardPage,
    dialog: OpportunityDialog,
    opportunity: OpportunityData,
) -> None:
    board.newOpportunityButton().click()
    dialog.fillOpportunity(opportunity.formValues())
    with page.expect_response(
        lambda response: response.url.endswith("/api/deals")
        and response.request.method == "POST",
    ) as responseInfo:
        dialog.submit()
    assert responseInfo.value.status == 201
    dialog.expectClosed()
    board.expectOpportunityInStage(STAGE_NAMES[opportunity.stage], opportunity.company)


def moveNext(page: Page, board: CrmBoardPage, stageName: str, company: str) -> Response:
    with page.expect_response(
        lambda response: "/move" in response.url
        and response.request.method == "PATCH",
    ) as responseInfo:
        board.moveOpportunity(stageName, company, "next")
    return responseInfo.value


def test_registration_to_access_rejects_zero_budget(
    page: Page,
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    opportunity = newOpportunity(value=0)
    createOpportunity(page, crmBoardPage, opportunityDialog, opportunity)

    response = moveNext(page, crmBoardPage, "Registration", opportunity.company)

    assert response.status == 400
    assert response.json()["message"] == (
        "Registration to Access requires customer information and a budget value."
    )
    crmBoardPage.expectOpportunityInStage("Registration", opportunity.company)
    crmBoardPage.expectOpportunityNotInStage("Access", opportunity.company)


@pytest.mark.parametrize("missingField", ["opportunityLocation", "expectedItems"])
def test_access_to_go_no_go_requires_location_and_expected_items(
    page: Page,
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
    missingField: str,
) -> None:
    opportunity = newOpportunity(
        stage="ACCESS",
        probability=45,
        **{missingField: ""},
    )
    createOpportunity(page, crmBoardPage, opportunityDialog, opportunity)

    response = moveNext(page, crmBoardPage, "Access", opportunity.company)

    assert response.status == 400
    assert response.json()["message"] == (
        "Access to Go - No Go requires opportunity location and expected items or materials."
    )
    crmBoardPage.expectOpportunityInStage("Access", opportunity.company)
    crmBoardPage.expectOpportunityNotInStage("Go - No Go", opportunity.company)


def test_go_no_go_to_award_requires_admin_and_allows_admin_approval(
    page: Page,
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    opportunity = newOpportunity(stage="GO_NO_GO", probability=25)
    createOpportunity(page, crmBoardPage, opportunityDialog, opportunity)
    crmBoardPage.setCurrentUser("J. Kim", "password")

    rejected = moveNext(page, crmBoardPage, "Go - No Go", opportunity.company)

    assert rejected.status == 403
    assert rejected.json()["message"] == (
        "Only an admin can approve a Go - No Go deal for Award."
    )
    crmBoardPage.expectOpportunityInStage("Go - No Go", opportunity.company)

    crmBoardPage.setCurrentUser("Admin", "Admin")
    approved = moveNext(page, crmBoardPage, "Go - No Go", opportunity.company)

    assert approved.status == 200
    assert approved.json()["stageCode"] == "AWARD"
    assert approved.json()["probability"] == 75
    crmBoardPage.expectOpportunityInStage("Award", opportunity.company)


def test_award_to_closed_requires_owner_or_admin(
    page: Page,
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    opportunity = newOpportunity(stage="AWARD", probability=75, owner="J. Kim")
    createOpportunity(page, crmBoardPage, opportunityDialog, opportunity)
    crmBoardPage.setCurrentUser("S. Lee", "password")

    rejected = moveNext(page, crmBoardPage, "Award", opportunity.company)

    assert rejected.status == 403
    assert rejected.json()["message"] == (
        "Only the deal owner or an admin can close an Award deal."
    )
    crmBoardPage.expectOpportunityInStage("Award", opportunity.company)

    crmBoardPage.setCurrentUser("J. Kim", "password")
    closed = moveNext(page, crmBoardPage, "Award", opportunity.company)

    assert closed.status == 200
    assert closed.json()["stageCode"] == "CLOSED"
    assert closed.json()["probability"] == 100
    crmBoardPage.expectOpportunityInStage("Closed", opportunity.company)
