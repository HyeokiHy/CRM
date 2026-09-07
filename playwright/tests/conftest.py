import os

import pytest
from playwright.sync_api import APIRequestContext, Page, Playwright

from pages.crm_board_page import CrmBoardPage
from pages.deal_dialog import DealDialog


@pytest.fixture
def baseUrl() -> str:
    return os.environ.get("CRM_BASE_URL", "http://localhost:8081")


@pytest.fixture
def apiRequestContext(
    playwright: Playwright,
    baseUrl: str,
) -> APIRequestContext:
    requestContext = playwright.request.new_context(base_url=baseUrl)
    yield requestContext
    requestContext.dispose()


@pytest.fixture
def crmBoardPage(page: Page, baseUrl: str) -> CrmBoardPage:
    boardPage = CrmBoardPage(page, baseUrl)
    boardPage.open()
    return boardPage


@pytest.fixture
def dealDialog(page: Page) -> DealDialog:
    return DealDialog(page)
