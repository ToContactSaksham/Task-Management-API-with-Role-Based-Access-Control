# Task Management API

Secure Spring Boot REST API for task management with JWT authentication and role-based access control.

## Features

- User registration/login with BCrypt password hashing and stateless JWTs.
- CRUD task endpoints with validation for title, description, due date, status, and assignee.
- `USER` accounts see their assigned tasks; `ADMIN` accounts can view all tasks and assign work to other users.
- Asynchronous assignment emails through `JavaMailSender`.
- PostgreSQL schema managed by Flyway.
- OpenAPI/Swagger UI at `/swagger-ui.html`.
- Docker Compose for the API, PostgreSQL, and Mailpit.

## Run locally

Requires Java 17, Maven, and PostgreSQL.

```bash
mvn spring-boot:run
```

Set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` as needed. The default development settings target PostgreSQL on `localhost:5432` with database `task_management`, user `postgres`, and password `postgres`.

## Run with Docker

```bash
mvn clean package
docker compose up --build
```

Mailpit is available at `http://localhost:8025`. Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

## API flow

1. `POST /api/auth/register` with `{ "name", "email", "password" }`.
2. `POST /api/auth/login` and copy the returned token.
3. Send `Authorization: Bearer <token>` to `/api/tasks`.

To create an admin, register a user and update its role in a controlled database/admin migration process; the public registration endpoint never accepts a role.
