# B2B CRM Pipeline

[![CRM test suite](https://github.com/HyeokiHy/CRM/actions/workflows/test.yml/badge.svg?branch=master)](https://github.com/HyeokiHy/CRM/actions/workflows/test.yml?query=branch%3Amaster)

[View Allure Test Report](https://hyeokihy.github.io/CRM/)

Spring Boot 기반의 B2B 영업기회 CRM 예제 프로젝트입니다.

CRM 애플리케이션을 직접 구현하고, 주요 비즈니스 규칙과 Validation을 대상으로
**pytest + Playwright 기반 UI/API 자동화 테스트**를 구성했습니다.

GitHub Actions에서 애플리케이션 실행부터 테스트, Allure Report 생성 및
GitHub Pages 배포까지 자동으로 수행하도록 CI 파이프라인을 구성했습니다.

영업기회는 아래 단계 흐름으로 관리합니다.

```text
Registration -> Access -> Go - No Go -> Award -> Closed
```

## 주요 기능

- 영업기회 생성, 조회, 수정, 삭제
- 단계별 파이프라인 보드
- 단계 이동 및 단계 이동 Validation
- 회사명, 담당자, Owner, 단계, 우선순위, 다음 액션 검색
- 단계 및 우선순위 필터
- 전체 Pipeline 금액, 가중 Forecast, Deal 수, Account 수 요약
- 로컬 사용자 생성
- 관리자 승인 및 Owner 권한 검증

## 테스트 자동화

CRM의 주요 Validation과 영업기회 단계 이동 규칙을
pytest + Playwright 기반으로 자동화했습니다.

### 자동화 범위

- Smoke Test
- Registration 필수값 Validation
- Registration -> Access
- Access -> Go - No Go
- Go - No Go -> Award
- Award -> Closed
- Deal API CRUD Lifecycle
- Deal Move API Validation

UI E2E 테스트에서는 실제 사용자 흐름과 단계별 Validation을 검증하고,
API 테스트에서는 CRUD Lifecycle과 단계 이동 API의 계약 및 상태 변경을 검증합니다.

### CI / Test Report

GitHub Actions에서 다음 과정을 자동으로 수행합니다.

```text
Spring Boot 실행
        ↓
pytest + Playwright
        ↓
Allure Results
        ↓
Allure HTML Report
        ↓
GitHub Pages
```

- CI: [GitHub Actions](https://github.com/HyeokiHy/CRM/actions)
- Test Report: [Allure Report](https://hyeokihy.github.io/CRM/)

## 기술 스택

### Application

- Java 17
- Spring Boot 3.3.13
- Spring Web
- Spring Data JPA
- H2 인메모리 DB: 기본 로컬 임시 실행용
- PostgreSQL: 실제 DB 실행용
- HTML / CSS / JavaScript

### Test Automation

- Python 3.13
- pytest
- Playwright
- Allure Report

### CI/CD

- GitHub Actions
- GitHub Pages

## 백엔드 구조

백엔드는 일반적인 Spring Layered Architecture 기반으로 정리했습니다.

기능별 패키지 안에서 Controller, Service, Repository, Entity 역할을 분리하고,
요청/응답 DTO와 수동 Mapper를 별도 패키지로 두어 API 응답에서 Entity를 직접 반환하지 않도록 구성했습니다.

## 기본 실행: H2 임시 DB

PostgreSQL 없이 바로 실행하려면 아래 명령어를 사용합니다.

```powershell
mvn spring-boot:run
```

브라우저에서 접속:

```text
http://localhost:8081
```

기본 DB는 H2 인메모리 DB입니다.
앱을 종료하면 데이터가 초기화됩니다.

H2 콘솔:

```text
http://localhost:8081/h2-console
```

H2 콘솔 접속 정보:

- JDBC URL: `jdbc:h2:mem:b2b_crm`
- User Name: `sa`
- Password: 비워두기

IntelliJ에서는 `B2bCrmApplication`을 실행하면 됩니다.
기본 H2 실행에는 별도 DB 환경변수가 필요 없습니다.

## 기본 사용자

아래 계정은 로컬 테스트를 위한 데모 계정입니다.

- 관리자: `Admin` / `Admin`
- 샘플 Owner: `J. Kim` / `password`
- 샘플 Owner: `S. Lee` / `password`
- 샘플 Owner: `M. Han` / `password`

## Windows 포트 자동 정리

Windows에서는 앱 시작 전에 설정된 서버 포트를 이미 사용 중인 프로세스를
`taskkill`로 자동 종료합니다.

로컬 개발 중 `Port 8081 was already in use` 오류를 줄이기 위한 기능입니다.

이 기능을 끄려면 VM option에 아래 값을 추가합니다.

```text
-Dlocal.port.cleaner.enabled=false
```

다른 포트로 실행하려면 program argument에 아래처럼 추가합니다.

```text
--server.port=18080
```

## PostgreSQL로 실행

PostgreSQL을 사용하려면 먼저 로컬에 PostgreSQL 서버가 설치 및 실행 중이어야 합니다.

필요한 것:

- PostgreSQL 서버
- 접속 가능한 DB 사용자
- `b2b_crm` 데이터베이스
- 해당 사용자에게 `b2b_crm` DB 권한

예시 SQL:

```sql
CREATE DATABASE b2b_crm;
CREATE USER crm_user WITH PASSWORD 'crm_password';
GRANT ALL PRIVILEGES ON DATABASE b2b_crm TO crm_user;
```

PowerShell 실행 예시:

```powershell
$env:DB_USERNAME="crm_user"
$env:DB_PASSWORD="crm_password"
$env:DB_URL="jdbc:postgresql://localhost:5432/b2b_crm"
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

IntelliJ에서 PostgreSQL 프로필로 실행하려면 Run Configuration에 아래 값을 설정합니다.

Environment variables:

```text
DB_USERNAME=crm_user;DB_PASSWORD=crm_password;DB_URL=jdbc:postgresql://localhost:5432/b2b_crm
```

Active profiles 또는 Program arguments:

```text
--spring.profiles.active=postgres
```

## API

- `GET /api/deals`
- `GET /api/deals/{id}`
- `POST /api/deals`
- `PUT /api/deals/{id}`
- `PATCH /api/deals/{id}/move`
- `DELETE /api/deals/{id}`
- `POST /api/users`

## 단계 이동 규칙

| 단계 이동 | Validation |
|---|---|
| `Registration -> Access` | 고객정보와 예산 Value 필요 |
| `Access -> Go - No Go` | 영업기회 장소와 수주 예정 물품/자재 필요 |
| `Go - No Go -> Award` | 관리자 승인 필요 |
| `Award -> Closed` | 영업기회를 만든 Owner 또는 관리자만 가능 |

## 보안 관련 참고

현재 사용자 인증은 **로컬 데모 및 테스트를 위한 단순 구현**입니다.

사용자 비밀번호는 데모 목적으로 평문 저장되며, 실제 운영 환경을 가정한 인증 시스템이 아닙니다.
Production 환경에서는 Spring Security와 BCrypt 등의 비밀번호 해시 및 인증/인가 체계가 필요합니다.