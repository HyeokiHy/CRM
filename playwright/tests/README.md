# CRM 테스트 자동화

## 테스트 자동화 개요

이 테스트 스위트는 Python, pytest, Playwright Sync API를 사용합니다. UI E2E 테스트는 브라우저 상호작용, 브라우저 기본 validation, 보드의 가시 상태를 검증합니다. API 테스트는 브라우저 없이 REST 계약과 서비스 수준 상태를 검증하며, 별도의 HTTP 라이브러리를 추가하지 않습니다.

## 테스트 구조

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

`pages/`는 반복되는 UI 상호작용과 locator를 관리합니다. `data/`는 재사용할 테스트 payload를 관리합니다. 각 테스트 파일은 business behavior와 assertion을 담당합니다.

## 테스트 시나리오

- **Smoke**: 5개 pipeline column이 렌더링되는지 확인하고, seed된 Registration Opportunity를 열어 dialog의 데이터가 채워지는지 확인합니다.
- **Registration validation**: Company, Contact, Owner, Next action 중 하나가 비어 있으면 제출을 차단하는지, 정상 Registration이 생성되어 보드에 표시되는지 검증합니다.
- **Registration → Access**: budget이 0인 경우 이동을 거부하고 Registration에 남기는지 검증합니다.
- **Access → Go - No Go**: Opportunity location과 Items or materials가 모두 필요한지 검증합니다.
- **Go - No Go → Award**: 일반 사용자를 거부하고 Admin을 허용하는지 검증합니다.
- **Award → Closed**: 비소유자를 거부하고 Deal owner를 허용하는지 검증합니다.
- **Deal API lifecycle**: 생성, 조회, 수정, 삭제 후 삭제된 resource의 not-found 응답을 검증합니다.
- **Move API contract**: Registration budget 규칙을 REST 경계에서 직접 검증합니다.

## Business Rule Mapping

| Business rule | Test scenario | Expected result |
| --- | --- | --- |
| 브라우저에서 Registration 필수값 요구 | Registration validation | 브라우저가 제출을 차단하고 dialog를 유지합니다. |
| Registration → Access는 고객 정보와 양수 value 필요 | UI move 및 Move API contract | `400`, service message, Registration 상태가 변경되지 않음 |
| Access → Go - No Go는 location과 expected items 필요 | UI move | `400`, service message, Access 상태가 변경되지 않음 |
| Go - No Go → Award는 Admin 필요 | UI authorization | 일반 사용자는 `403`, Admin은 `200`, Award 및 probability 75 |
| Award → Closed는 owner 또는 Admin 필요 | UI authorization | 비소유자는 `403`, owner는 `200`, Closed 및 probability 100 |
| Deal CRUD endpoint는 resource 상태를 반영 | Deal API lifecycle | 생성 `201`, 조회/수정 `200`, 삭제 `204`, 이후 조회 `404` |

위 status code와 message는 `DealController`, `DealService`, `GlobalExceptionHandler` 구현을 기준으로 작성했습니다.

## 테스트 데이터

`newOpportunity()`는 UUID suffix가 포함된 company name을 생성합니다. 테스트가 서로의 카드를 선택하거나 동일 resource를 덮어쓰는 일을 방지합니다.

현재 UI E2E 테스트는 생성한 Deal을 실행 중인 H2 instance에 남깁니다. 기본 H2 database는 애플리케이션이 종료되면 다시 생성됩니다. API 테스트는 `finally`에서 생성한 resource를 삭제합니다. persistent database 또는 병렬 CI를 도입할 때는 database reset 전략 또는 범위가 제한된 cleanup fixture를 추가해야 합니다. 이 작업에서는 그 구조를 의도적으로 추가하지 않았습니다.

## 테스트 실행

저장소 root에서 Spring Boot를 실행합니다.

```bash
mvn spring-boot:run
```

기존 의존성과 Chromium을 설치합니다.

```bash
python3 -m pip install -r playwright/requirements.txt
python3 -m playwright install chromium
```

저장소 root에서 전체 테스트를 실행합니다.

```bash
python3 -m pytest
```

기본 URL은 `http://localhost:8081`입니다. 다른 실행 중인 instance를 대상으로 실행하려면 `CRM_BASE_URL`을 설정합니다.

## UI E2E와 API 테스트의 역할

UI E2E 테스트는 실제 사용자 상호작용, 브라우저 기본 validation, 보드 상태를 검증합니다. API 테스트는 HTTP status, request/response 형식, service validation, 영속된 resource 상태를 검증합니다.

API 테스트는 모든 UI flow를 중복하지 않습니다. CRUD와 대표적인 stage transition REST contract를 제공하고, 브라우저 테스트는 사용자에게 보이는 동작과 authorization journey를 담당합니다.
