import pytest
from playwright.sync_api import Page

from pages.crm_board_page import CrmBoardPage
from pages.opportunity_dialog import OpportunityDialog


@pytest.fixture
def baseUrl() -> str:
    return "http://localhost:8081"


@pytest.fixture
def crmBoardPage(
    page: Page,
    baseUrl: str,
) -> CrmBoardPage:
    boardPage = CrmBoardPage(page, baseUrl)
    boardPage.open()
    return boardPage


@pytest.fixture
def opportunityDialog(page: Page) -> OpportunityDialog:
    return OpportunityDialog(page)
