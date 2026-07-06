# B2B CRM Pipeline

A Spring Boot CRUD CRM for managing B2B opportunities through:

1. Registration
2. Go - No Go
3. Access
4. Award
5. Closed

## Features

- Create, read, update, and delete opportunities
- Move deals forward or backward across pipeline stages
- Search by company, contact, owner, stage, priority, or next action
- Filter by stage and priority
- Track total pipeline value, weighted forecast, total deals, and active accounts
- Create local users and enforce stage transition validation

## Stack

- Spring Boot 2.7
- Spring Web
- Spring Data JPA
- H2 in-memory database for local temporary runs
- MySQL
- Static HTML/CSS/JavaScript served by Spring Boot

## Run

For a temporary local database, just run:

```powershell
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

The default database is H2 in-memory, so data resets when the app stops. You can inspect it at:

```text
http://localhost:8080/h2-console
```

H2 console settings:

- JDBC URL: `jdbc:h2:mem:b2b_crm`
- User Name: `sa`
- Password: leave empty

In IntelliJ, run `B2bCrmApplication`. You do not need any MySQL environment variables for the default H2 run.

Default users:

- Admin: `Admin` / `Admin`
- Sample owners: `J. Kim` / `password`, `S. Lee` / `password`, `M. Han` / `password`

On Windows, the app automatically runs `taskkill` against an existing process that is already listening on the configured server port before Spring Boot starts. This prevents the common `Port 8080 was already in use` startup error during local development.

To disable that behavior, add this VM option:

```text
-Dlocal.port.cleaner.enabled=false
```

If you want to run on another port, add this program argument in the run configuration:

```text
--server.port=18080
```

## Run With MySQL

Create or start a local MySQL server, then run with the `mysql` profile:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-password"
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

You can override the database URL with `DB_URL`.

## API

- `GET /api/deals`
- `GET /api/deals/{id}`
- `POST /api/deals`
- `PUT /api/deals/{id}`
- `PATCH /api/deals/{id}/move`
- `DELETE /api/deals/{id}`
- `POST /api/users`

## Stage Rules

The active forward flow is:

```text
Registration -> Access -> Go - No Go -> Award -> Closed
```

- Registration to Access requires customer information and budget value.
- Access to Go - No Go requires opportunity location and expected items or materials.
- Go - No Go to Award requires admin approval.
- Award to Closed requires the deal owner or an admin.
