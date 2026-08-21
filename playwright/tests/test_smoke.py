from playwright.sync_api import expect

from pages.crm_board_page import CrmBoardPage
from pages.opportunity_dialog import OpportunityDialog


def test_pipeline_renders_all_stages_and_opens_existing_opportunity(
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    for stageName in ("Registration", "Access", "Go - No Go", "Award", "Closed"):
        expect(crmBoardPage.stageColumn(stageName)).to_have_count(1)

    crmBoardPage.dealCard(
        "Registration",
        "Atlas Components",
    ).get_by_role("button", name="Atlas Components", exact=False).click()

    opportunityDialog.expectOpen()
    assert opportunityDialog.field("Company").input_value() == "Atlas Components"
    assert opportunityDialog.field("Owner").input_value() == "J. Kim"
