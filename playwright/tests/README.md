# CRM test automation

## Test Automation Overview

The suite uses Python, pytest, and Playwright Sync API. UI E2E tests cover browser interaction, native validation, and visible board state. API tests cover REST contracts and service-level state without a browser; no additional HTTP library is used.

## Test Structure

```text
playwright/tests/
├── api/
│   └── test_deals_api.py
├── conftest.py
├── data/
│   └── opportunity_data.py
├── pages/
│   ├── crm_board_page.py
│   └── opportunity_dialog.py
├── test_smoke.py
├── test_registration_validation.py
└── test_opportunity_stage_transition.py
```

`pages/` owns repeatable UI interactions and locators. `data/` owns reusable test payloads. Test files own business behavior and assertions.

## Test Scenarios

- Smoke: renders five pipeline columns and opens a seeded Registration opportunity.
- Registration validation: blocks an empty Company, Contact, Owner, or Next action; a valid Registration is created and displayed.
- Registration → Access: rejects a zero budget and keeps the deal in Registration.
- Access → Go - No Go: requires Opportunity location and Items or materials.
- Go - No Go → Award: rejects a non-admin and allows an Admin.
- Award → Closed: rejects a non-owner and allows the Deal owner.
- Deal API lifecycle: creates, reads, updates, deletes, then verifies the post-delete not-found response.
- Move API contract: verifies the Registration budget rule directly at the REST boundary.

## Business Rule Mapping

| Business rule | Test scenario | Expected result |
| --- | --- | --- |
| Registration fields are required in the browser | Registration validation | Browser blocks submission and keeps the dialog open. |
| Registration → Access requires customer information and positive value | UI move and Move API contract | `400`, service message, and Registration state remain unchanged. |
| Access → Go - No Go requires location and expected items | UI move | `400`, service message, and Access state remain unchanged. |
| Go - No Go → Award requires Admin | UI authorization | Non-admin receives `403`; Admin receives `200`, Award, and probability 75. |
| Award → Closed requires owner or Admin | UI authorization | Non-owner receives `403`; owner receives `200`, Closed, and probability 100. |
| Deal CRUD endpoints persist resource state | Deal API lifecycle | Create `201`, reads/updates `200`, delete `204`, then read `404`. |

The status codes and messages are taken from `DealController`, `DealService`, and `GlobalExceptionHandler`.

## Test Data

`newOpportunity()` creates a UUID-suffixed company name. It prevents tests from selecting one another's cards or overwriting the same resource.

Current UI E2E tests leave their created deals in the running H2 instance. The default H2 database is recreated when the application stops. API tests delete their resources, including cleanup in `finally`. For a persistent database or parallel CI, add a controlled database reset strategy or scoped cleanup fixture; that work is intentionally not introduced here.

## Test Execution

Start Spring Boot from the repository root:

```bash
mvn spring-boot:run
```

Install the existing dependencies and Chromium:

```bash
python3 -m pip install -r playwright/requirements.txt
python3 -m playwright install chromium
```

Run all tests from the repository root:

```bash
python3 -m pytest
```

The default URL is `http://localhost:8081`. Set `CRM_BASE_URL` to target another running instance.

## UI E2E and API Test Boundaries

UI E2E tests cover actual user interaction, browser-native validation, and board state. API tests cover HTTP status, request/response shape, service validation, and persisted resource state.

The API suite deliberately does not duplicate every UI flow: it provides CRUD coverage and one direct stage-transition contract, while the browser suite remains responsible for visible user behavior and authorization journeys.
