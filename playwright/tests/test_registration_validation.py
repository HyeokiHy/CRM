import pytest

from pages.crm_board_page import CrmBoardPage
from pages.opportunity_dialog import OpportunityDialog


@pytest.mark.skip(
    reason="TODO: Implement Registration validation scenarios manually.",
)
def test_registration_required_fields(
    crmBoardPage: CrmBoardPage,
    opportunityDialog: OpportunityDialog,
) -> None:
    crmBoardPage.newOpportunityButton().click()
    opportunityDialog.expectOpen()

    # TODO: Submit the form after deciding which default values must be cleared.
    # TODO: Assert each validation rule confirmed from actual UI behavior.
    pytest.fail("TODO: Registration validation is not implemented yet.")
