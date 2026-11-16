# Team Project Manager

Spring Boot REST API for project, membership, and task management, with JWT authentication and role-based authorization.

## Tech stack

* Java 21
* Spring Boot 4.0.1
* Spring Security
* JJWT 0.13.0
* Spring Data JPA / Hibernate
* PostgreSQL
* Flyway
* Maven
* JUnit, Mockito, MockMvc, H2

## Features

* User registration and login with JWT authentication.
* Project creation, retrieval, listing, and deletion.
* Project membership management with OWNER, ADMIN, and MEMBER roles.
* Role-based authorization.
* Task creation with an initial TODO status.
* Versioned database migrations.
* Unit and integration tests.

Current scope: Task listing, updating, assignment, and deletion are not exposed as API operations.

## Architecture

The application follows a layered, feature-oriented structure:

* Controllers: HTTP endpoints.
* Services: Business logic and authorization.
* Repositories: Data access through Spring Data JPA.
* Security: JWT processing and request authentication.
* DTOs: API request and response models.
* Migrations: SQL files in src/main/resources/db/migration.

## API

Registration and login are public. All other endpoints require a bearer token.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a user |
| POST | `/auth/login` | Authenticate a user | 
| POST | `/projects` | Create a project |
| GET | `/projects` | List accessible projects |
| GET | `/projects/{id}` | Retrieve a project |
| DELETE | `/projects/{id}` | Delete a project |
| POST | `/projects/{projectId}/members` | Add a member |
| DELETE | `/projects/{projectId}/members/{userId}` | Remove a member |
| POST | `/projects/{projectId}/tasks` | Create a task |

Protected requests require:

`Authorization: Bearer <token>`

# Getting started

## Prerequisites

* Debian or Ubuntu
* JDK 21
* PostgreSQL
* Git

Maven is included through the Maven Wrapper.

## 1. Install dependencies

```bash
sudo apt update
sudo apt install -y git openjdk-21-jdk postgresql postgresql-client
```

Verify Java and Git:

java -version
git --version

## 2. Clone the repository

```bash
git clone https://github.com/croookie/project-manager.git
cd project-manager
```

## 3. Configure PostgreSQL

Start PostgreSQL:

```bash
sudo service postgresql start
```

Set the PostgreSQL cluster to use the default port 5432:

```bash
sudo pg_lsclusters
```

Find your PostgreSQL version and cluster name in the output. For a standard installation with PostgreSQL 17:

```bash
sudo pg_ctlcluster 17 main stop
sudo pg_conftool 17 main set port 5432
sudo pg_ctlcluster 17 main start
```

Replace 17 with your installed PostgreSQL major version if necessary.

Verify that PostgreSQL is accepting connections on port 5432:

```bash
pg_isready -h 127.0.0.1 -p 5432
```

## 4. Create the database and role

Open a PostgreSQL administrative session:

`sudo -u postgres psql`

Create the application role and database:

```sql
CREATE ROLE project_manager_user
WITH LOGIN PASSWORD 'your_secure_password';
CREATE DATABASE project_manager_db
OWNER project_manager_user;
```

Exit with `\q`.

## 5. Configure environment variables

```bash
export DB_USERNAME="project_manager_user"
export DB_PASSWORD="your_secure_password"
export JWT_SECRET="your_random_secret"
```

`JWT_SECRET` must be at least 32 bytes for the configured HMAC algorithm.

## 6. Apply database migrations

```bash
./mvnw flyway:migrate`
```

Flyway connects to `localhost:5432/project_manager_db` using `DB_USERNAME` and `DB_PASSWORD`.

## 7. Start the application

```bash
./mvnw spring-boot:run
```

The application uses the same database credentials and the default PostgreSQL connection at `localhost:5432/project_manager_db`.

## Tests

Run the test suite:

```bash
./mvnw test
```

Or run the full Maven verification lifecycle:

```bash
./mvnw clean verify
```

Tests use the test Spring profile and an in-memory H2 database. Integration tests use MockMvc; service unit tests use Mockito.

## Project structure

```
src/
├── main/
│   ├── java/com/max/team_project_manager/
│   │   ├── auth/
│   │   ├── common/
│   │   ├── exception/
│   │   ├── membership/
│   │   ├── project/
│   │   ├── security/
│   │   ├── task/
│   │   └── user/
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    ├── java/
    └── resources/
        └── application-test.properties

```

# Configuration

* PostgreSQL must be running before migrations or application startup.
* Flyway migrations are applied manually using ./mvnw flyway:migrate.
* Database credentials and JWT secrets should be supplied through environment variables and never committed to the repository.
