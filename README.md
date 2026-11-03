# Team Project Manager

A Spring Boot REST API for managing projects, project memberships, and tasks. The application uses JWT-based authentication and role-based authorization to control access to project operations.

## Tech stack

- **Java 21**
- **Spring Boot 4.0.1**
- **Spring Security** for request authentication and authorization
- **JWT (JJWT 0.13.0)** for token-based authentication
- **Spring Data JPA / Hibernate** for persistence
- **PostgreSQL** for the application database
- **Flyway** for database schema migrations
- **Maven** for dependency management and builds
- **JUnit, Mockito, MockMvc, and H2** for automated tests

## Features

- User registration and login.
- JWT-based authentication for protected endpoints.
- Project creation, retrieval, listing, and deletion.
- Project membership management.
- Project roles: `OWNER`, `ADMIN`, and `MEMBER`.
- Role-based checks for membership and task operations.
- Task creation with an initial `TODO` status.
- Versioned database schema managed by Flyway.
- Unit tests for service-level business rules and integration tests for HTTP and persistence behavior.

> **Current scope:** task creation is implemented. Task listing, updating, assignment, and deletion are not currently exposed as API operations.

## Architecture

The codebase is organized by feature, with responsibilities separated into controller, service, repository, and data-transfer layers.

- **Controllers** expose the HTTP API.
- **Services** implement application rules and coordinate operations.
- **Repositories** provide database access through Spring Data JPA.
- **Security** contains JWT handling, the authentication filter, and current-user access.
- **DTOs** represent request and response payloads.
- **Flyway migrations** define the PostgreSQL schema in `src/main/resources/db/migration`.

## API overview

All endpoints except registration and login require a valid bearer token.

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/auth/register` | Register a user and receive an authentication token |
| `POST` | `/auth/login` | Authenticate and receive an authentication token |
| `POST` | `/projects` | Create a project |
| `GET` | `/projects` | List projects available to the authenticated user |
| `GET` | `/projects/{id}` | Retrieve a project |
| `DELETE` | `/projects/{id}` | Delete a project |
| `POST` | `/projects/{projectId}/members` | Add a project member |
| `DELETE` | `/projects/{projectId}/members/{userId}` | Remove a project member |
| `POST` | `/projects/{projectId}/tasks` | Create a task in a project |

For protected requests, provide the token in the header:

```http
Authorization: Bearer <token>
```

Request validation and authorization rules are enforced by the application. The exact permissions depend on the operation and the caller's project role.

## Getting started

### Prerequisites

- JDK 21
- PostgreSQL
- Git

The repository includes the Maven Wrapper, so a separate Maven installation is not required.

### 1. Clone the repository

```bash
git clone <repository-url>
cd <repository-directory>
```

Replace the placeholders with the repository URL and directory name.

### 2. Create the PostgreSQL database

For example, using `createdb`:

```bash
createdb project_manager_db
```

Alternatively, create a database named `project_manager_db` using your PostgreSQL client.

### 3. Configure environment variables

The application reads its database credentials and JWT signing secret from environment variables:

| Variable | Purpose |
|---|---|
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | Secret used to sign JWTs |

Set them in your shell before starting the application. For example:

```bash
export DB_USERNAME="your_database_user"
export DB_PASSWORD="your_database_password"
export JWT_SECRET="replace_with_a_long_random_secret"
```

`JWT_SECRET` must be sufficiently long for the configured HMAC signing algorithm. Use a securely generated secret of at least 32 bytes. Do not commit real credentials or secrets to the repository.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

On Windows, use:

```powershell
.\mvnw.cmd spring-boot:run
```

Flyway applies the versioned migrations from `src/main/resources/db/migration` when the application starts.

The application uses the PostgreSQL database configured in `src/main/resources/application.properties` (`localhost:5432/project_manager_db` by default).

## Running tests

Run the test suite with:

```bash
./mvnw test
```

Or run the Maven verification lifecycle:

```bash
./mvnw clean verify
```

The integration tests activate the `test` Spring profile using `@ActiveProfiles("test")`. The profile configuration in `src/test/resources/application-test.properties` uses an in-memory H2 database, separate from the PostgreSQL database used by the application.

The integration tests use MockMvc to exercise HTTP endpoints and verify persisted state. Service unit tests use Mockito to isolate business logic and verify authorization behavior.

## Project layout

```text
src/
├── main/
│   ├── java/com/max/team_project_manager/
│   │   ├── auth/          # Registration and login
│   │   ├── common/        # Shared API response handling
│   │   ├── exception/     # Application exceptions
│   │   ├── membership/    # Project membership and roles
│   │   ├── project/       # Project API and business logic
│   │   ├── security/      # JWT and Spring Security
│   │   ├── task/          # Task creation and task models
│   │   └── user/          # User API and persistence
│   └── resources/
│       ├── db/migration/  # Flyway SQL migrations
│       └── application.properties
└── test/
    ├── java/              # Unit and integration tests
    └── resources/
        └── application-test.properties
```

## Notes

- The default application configuration expects PostgreSQL to be available locally.
- Tests use H2 through the `test` profile and should not require the application PostgreSQL database.
- Keep local secrets in environment variables or an untracked local configuration file. Never commit production credentials.
