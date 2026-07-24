import pytest
from playwright.sync_api import Page, expect

STAGES = [
    ("Registration", "REGISTRATION"),
    ("Access", "ACCESS"),
    ("Go - No Go", "GO_NO_GO"),
    ("Award", "AWARD"),
    ("Closed", "CLOSED"),
]

@pytest.mark.parametrize(
    ("stageName", "stageCode"),
    STAGES,
)

def test_opportunity_open_close(
        page: Page,
        stageName: str,
        stageCode: str, ) :
    page.goto("http://localhost:8081")

    stageColumn = page.locator(".stage-column").filter(
        has=page.get_by_role(
            "heading",
            name=stageName,
            exact=True,
        ),
    )

    expect(stageColumn).to_have_count(1)

    opportunityButtons = stageColumn.locator(".deal-main")

    assert opportunityButtons.count() > 0, (
        f"No Opportunity exists in stage : "
        f"{stageName} ({stageCode})"
    )

    opportunityButtons.first.click()

    dialog = page.get_by_role("dialog")

    expect(dialog).to_be_visible()
    expect(dialog.get_by_label("Company")).not_to_have_value("")
    expect(dialog.get_by_label("Owner")).not_to_have_value("")

    dialog.get_by_role(
        "button",
        name="Close",
        exact=True,
    ).click()

    expect(dialog).not_to_be_visible()