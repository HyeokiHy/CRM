# B2B CRM Quality Engineering Portfolio

[![CRM test suite](https://github.com/HyeokiHy/CRM/actions/workflows/test.yml/badge.svg?branch=master)](https://github.com/HyeokiHy/CRM/actions/workflows/test.yml?query=branch%3Amaster)

**[Allure Test Report 보기](https://hyeokihy.github.io/CRM/)**

Spring Boot 기반 B2B CRM을 직접 구현하고, 단계 전환에 얽힌 Validation과 Authorization을 pytest + Playwright로 검증한 QA Engineering 포트폴리오입니다. UI/API 자동화, GitHub Actions 기반 회귀 검증, Allure Report, Docker Compose 실행 환경을 통해 Business Rule을 재현하고 결과를 확인할 수 있도록 구성했습니다.

## What This Project Demonstrates

- **Business Rule 기반 Test Analysis & Design**: CRM Stage Transition 조건과 실패 위험을 테스트 시나리오로 연결
- **Playwright UI E2E Automation**: 브라우저 입력 Validation, 사용자 흐름, 화면의 최종 상태를 검증
- **REST API Automation**: Playwright `APIRequestContext`로 CRUD contract와 대표적인 이동 규칙을 직접 검증
- **Positive / Negative & Authorization Testing**: 거부 응답뿐 아니라 상태 불변, 허용 사용자, 변경된 probability까지 확인
- **Independent Test Data**: UUID가 포함된 company name을 생성하고 API resource는 `finally`에서 정리
- **Continuous Regression & Reproducibility**: GitHub Actions/Allure와 Docker Compose 기반 실행 환경 제공

## Quality Engineering Approach

이 프로젝트의 핵심 품질 위험은 Deal이 필요한 정보나 권한 없이 다음 Stage로 이동하는 것입니다. `DealService`는 현재 Stage와 목표 Stage의 조합에 따라 Business Rule을 적용하고, 테스트는 HTTP 응답과 보드 상태를 함께 확인합니다.

| 품질 위험 | 구현된 규칙 | 검증 방식 |
| --- | --- | --- |
| 불완전한 고객정보로 영업 진행 | Registration → Access에 Company, Contact, 양수 Value 필요 | UI/API negative test에서 `400`과 Stage 불변 확인 |
| 실행 정보 없이 입찰 판단 | Access → Go - No Go에 Location과 Expected Items 필요 | 각 누락 필드를 parameterize하여 거부와 상태 불변 확인 |
| 승인 권한 우회 | Go - No Go → Award는 Admin만 허용 | 일반 사용자 `403` 후 Admin 성공과 probability `75` 확인 |
| 타인의 Deal 종료 | Award → Closed는 Owner 또는 Admin만 허용 | 비소유자 `403` 후 Owner 성공과 probability `100` 확인 |
| 잘못된 생성/수정 입력 | DTO Bean Validation과 HTML required/min/max 적용 | UI 필수값 차단 및 API 응답/상태 assertion |

UI E2E는 실제 브라우저 입력, dialog, card 이동처럼 사용자에게 보이는 동작을 담당합니다. API test는 UI 흐름을 반복하기보다 status code, response body, CRUD lifecycle, service validation을 REST 경계에서 빠르게 확인합니다. Negative scenario에서는 오류 메시지만 보지 않고 기존 Stage가 유지되는지도 확인하며, 권한 시나리오는 거부와 허용 경로를 한 흐름에서 검증합니다.

테스트 데이터는 `newOpportunity()`가 UUID suffix를 붙여 생성하므로 다른 테스트의 card를 잘못 선택할 가능성을 줄입니다. API test는 생성한 데이터를 정리하지만 UI E2E 데이터는 실행 중인 DB에 남습니다. 기본 H2는 애플리케이션 종료 시 초기화되며, persistent DB의 병렬 실행을 위한 별도 reset 전략은 아직 없습니다.

## Automated Test Coverage

| Scenario | Layer | Quality Risk |
| --- | --- | --- |
| 5개 Stage column과 seed Deal dialog 표시 | UI Smoke | 핵심 화면 또는 초기 데이터 로딩 실패 |
| Company, Contact, Owner, Next Action 필수값 및 정상 생성 | UI E2E | 브라우저 Validation 우회, 생성 결과 미표시 |
| Registration → Access: Value `0` 거부 | UI E2E | 예산 없는 Deal의 잘못된 진행 |
| Access → Go - No Go: Location/Items 각각 누락 | UI E2E | 필수 실행 정보가 없는 상태 전환 |
| Go - No Go → Award: 일반 사용자 거부, Admin 허용 | UI E2E | 관리자 승인 우회 |
| Award → Closed: 비소유자 거부, Owner 허용 | UI E2E | 소유권 없는 사용자에 의한 종료 |
| Deal 생성 → 조회 → 수정 → 삭제 → `404` | REST API | CRUD contract와 resource lifecycle 회귀 |
| Registration 이동 API의 양수 Value 규칙 | REST API | service validation과 상태 불변 회귀 |

상세 시나리오와 Business Rule mapping은 [`playwright/tests/README.md`](playwright/tests/README.md)에서 확인할 수 있습니다.

## Automation Architecture

```text
playwright/tests/
├── api/test_deals_api.py          # REST contract와 resource state assertion
├── data/opportunity_data.py       # UUID 기반 test data와 UI/API payload
├── pages/
│   ├── crm_board_page.py          # 보드 locator와 card/user interaction
│   └── opportunity_dialog.py      # dialog locator, 입력, 제출 동작
├── conftest.py                    # base URL, browser page, API request context fixture
├── test_smoke.py
├── test_registration_validation.py
└── test_opportunity_stage_transition.py
```

Page Object는 반복되는 locator와 UI interaction을 캡슐화하고, 각 test는 Business Behavior와 assertion을 소유합니다. API는 별도 client wrapper를 만들지 않고 Playwright의 `APIRequestContext` fixture를 직접 사용합니다. 대상 URL은 `CRM_BASE_URL`로 바꿀 수 있고 기본값은 `http://localhost:8081`입니다.

## CI & Test Reporting

GitHub Actions의 `CRM test suite`는 Java 17, Python 3.13, Chromium, Allure CLI를 준비한 뒤 H2 profile로 Spring Boot를 실행합니다. HTTP 응답을 polling해 준비 상태를 확인한 후 pytest를 실행하므로 고정된 sleep에만 의존하지 않습니다.

```text
Application startup
→ pytest + Playwright UI/API tests
→ Allure Results + Spring Boot log artifact
→ Allure HTML Report
→ GitHub Pages (master push)
```

- [GitHub Actions 실행 결과](https://github.com/HyeokiHy/CRM/actions/workflows/test.yml?query=branch%3Amaster)
- [Allure HTML Report](https://hyeokihy.github.io/CRM/)

Docker 도입 후에도 CI는 더 단순한 기존 H2 방식을 유지합니다. Compose는 reviewer가 PostgreSQL까지 포함한 환경을 로컬에서 재현하기 위한 별도 실행 경로입니다.

## Quick Start with Docker

Docker Desktop 또는 Docker Engine + Compose plugin이 필요합니다.

```bash
docker compose up --build
```

- Web: http://localhost:8081
- REST API: http://localhost:8081/api/deals

Compose는 PostgreSQL healthcheck가 성공한 뒤 `postgres` Spring Profile로 Application을 시작합니다. 기본 DB 계정은 **로컬 데모 전용 값**이며 실제 Secret이 아닙니다. 값을 바꾸려면 `.env.example`을 `.env`로 복사해 수정하세요. `.env`는 Git에서 제외됩니다.

Application이 실행 중일 때 host에서 같은 자동화 suite를 실행할 수 있습니다.

```bash
python -m pip install -r playwright/requirements.txt
python -m playwright install chromium
python -m pytest
```

종료:

```bash
docker compose down
```

`postgres-data` volume은 다음 실행에도 유지됩니다. 데이터를 함께 초기화하려면 `docker compose down -v`를 사용합니다.

## Application Domain

```text
Registration → Access → Go - No Go → Award → Closed
```

CRM은 영업기회의 생성·조회·수정·삭제, Stage별 pipeline board, 검색/필터, 금액 summary를 제공합니다. Stage 이동 시 고객정보와 예산, 기회 장소와 예상 품목, Admin 승인, Owner/Admin 권한을 순차적으로 확인합니다.

데모 사용자:

- Admin: `Admin` / `Admin`
- Owner: `J. Kim`, `S. Lee`, `M. Han` / `password`

위 credential은 로컬 테스트를 위한 seed data입니다.

## Tech Stack

| 영역 | 기술 |
| --- | --- |
| Application | Java 17, Spring Boot 3.3.13, Spring Web, Spring Data JPA, HTML/CSS/JavaScript |
| Database | H2 (기본 로컬/CI), PostgreSQL (`postgres` profile 및 Docker Compose) |
| Test Automation | Python 3.13, pytest, Playwright, Allure Report |
| Infrastructure | Docker, Docker Compose, GitHub Actions, GitHub Pages |

## 기존 로컬 실행 방식

H2 인메모리 DB는 그대로 기본값입니다.

```bash
mvn spring-boot:run
```

- Web: http://localhost:8081
- H2 Console: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:b2b_crm`, User: `sa`, Password: 비워두기

로컬 PostgreSQL은 기존 `postgres` profile과 환경변수로 실행할 수 있습니다.

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/b2b_crm"
$env:DB_USERNAME="crm_user"
$env:DB_PASSWORD="<local-password>"
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Windows의 개발 편의를 위한 port cleaner는 유지됩니다. 필요하면 `-Dlocal.port.cleaner.enabled=false`로 끌 수 있습니다.

## Limitations

- 인증은 Business Rule 시연을 위한 단순 username/password 비교이며 Spring Security를 사용하지 않습니다.
- 데모 사용자 비밀번호는 평문으로 저장됩니다. 실제 서비스라면 hashing, secret 관리, session/token, authorization 정책이 필요합니다.
- UI E2E가 생성한 데이터의 개별 cleanup과 persistent DB의 reset/parallel isolation 전략은 구현되어 있지 않습니다.
- 현재 자동화 범위는 주요 Stage Rule과 Deal CRUD 중심이며 성능, 접근성, 보안 테스트는 포함하지 않습니다.
- Docker Compose의 기본 credential은 로컬 데모 전용입니다. 운영 환경 배포 구성을 의미하지 않습니다.
