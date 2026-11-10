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

Method	Endpoint	Description
POST	/auth/register	Register a user
POST	/auth/login	Authenticate a user
POST	/projects	Create a project
GET	/projects	List accessible projects
GET	/projects/{id}	Retrieve a project
DELETE	/projects/{id}	Delete a project
POST	/projects/{projectId}/members	Add a member
DELETE	/projects/{projectId}/members/{userId}	Remove a member
POST	/projects/{projectId}/tasks	Create a task

Protected requests require:

`Authorization: Bearer <token>`

# Getting started

## Prerequisites

* JDK 21
* PostgreSQL
* Git

Maven is included through the Maven Wrapper.

## 1. Clone the repository

```bash
git clone https://github.com/croookie/project-manager.git
cd project-manager
```

## 2. Install PostgreSQL

On Debian/Ubuntu:

```bash
sudo apt update
sudo apt install postgresql
sudo service postgresql start
```

Verify the server is running:

`pg_isready`

## 3. Create the database and role

Open a PostgreSQL administrative session:

`sudo -u postgres psql`

Create a role and database:

```sql
CREATE ROLE project_manager_user
WITH LOGIN PASSWORD 'your_secure_password';
CREATE DATABASE project_manager_db
OWNER project_manager_user;
```

Exit with `\q`.

## 4. Configure environment variables

Set the database credentials and JWT signing secret:

```bash
export DB_USERNAME="project_manager_user"
export DB_PASSWORD="your_secure_password"
export JWT_SECRET="your_random_secret"
```

`JWT_SECRET` must be at least 32 bytes for the configured HMAC algorithm.

## 5. Apply database migrations

Run Flyway through the Maven plugin:

`./mvnw flyway:migrate`

The plugin connects to localhost:5432/project_manager_db using DB_USERNAME and DB_PASSWORD.

## 6. Start the application

`./mvnw spring-boot:run`

The application uses the same database credentials and the default PostgreSQL connection at localhost:5432/project_manager_db.

## Tests

Run the test suite:

`./mvnw test`

Or run the full Maven verification lifecycle:

`./mvnw clean verify`

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
