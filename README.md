# Hisab API — Finance Management System

A RESTful backend API for personal and group-based expense tracking. Built with Spring Boot 3, secured with JWT and OAuth2, and backed by PostgreSQL.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security, JWT (JJWT 0.11), OAuth2 |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Mapping | ModelMapper |
| Build | Maven |
| Runtime | Docker (multi-stage) |

---

## Features

- **Authentication** — Register, login with JWT, and password reset
- **OAuth2 Social Login** — Google, GitHub, and Twitter
- **Groups** — Create and manage expense groups, pin groups
- **Items** — Track personal expenses or attach items to a group
- **Sharing** — Share groups with other users and accept/decline share requests
- **Pagination** — All list endpoints support Spring Data `Pageable`

---

## Project Structure

```
src/main/java/com/fms/
├── config/         # Security, JWT filter, OAuth2 success handler
├── controller/     # REST controllers
├── dto/            # Request/Response DTOs
├── entity/         # JPA entities
├── globalException/# Global exception handler, custom auth handlers
├── repository/     # Spring Data JPA repositories
├── service/        # Business logic
└── util/           # API response wrappers, helpers
```

---

## API Reference

### Auth — `/auth`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Register a new user |
| `POST` | `/auth/login` | Public | Login and receive a JWT |
| `PATCH` | `/auth/{userName}/reset-password?newPassword=` | Public | Reset user password |

### Groups — `/groups`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/groups` | Required | Get own and shared groups |
| `GET` | `/groups/all` | Required | Get all groups (paginated) |
| `GET` | `/groups/{id}/items` | Required | Get all items in a group (paginated) |
| `POST` | `/groups` | Required | Create a new group |
| `POST` | `/groups/{id}/item` | Required | Add an item to a group |
| `PUT` | `/groups/{id}` | Required | Update a group |
| `DELETE` | `/groups/{id}` | Required | Delete a group |

### Items — `/items`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/items` | Required | Get all items for the current user (paginated) |
| `GET` | `/items/personal` | Required | Get personal (non-group) items (paginated) |
| `POST` | `/items/personal` | Required | Create a personal item |
| `PUT` | `/items/{id}` | Required | Update an item |
| `DELETE` | `/items/{id}` | Required | Delete an item |

### Shares — `/shares`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/shares/group` | Required | Share a group with another user |
| `PATCH` | `/shares/{id}/accept` | Required | Accept a share request |

### Users — `/users`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/users` | Required | Get all users |

---

## Authentication

All protected endpoints require a `Bearer` token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

Obtain a token via `POST /auth/login`.

OAuth2 login is available at `/login/oauth2/code/{provider}` for `google`, `github`, and `twitter`.

---

## Configuration

Copy `application.properties` and set the following environment-specific values:

```properties
# Database
spring.datasource.url=jdbc:postgresql://<host>:<port>/<db>
spring.datasource.username=<username>
spring.datasource.password=<password>

# JWT
jwt.secret=<base64-encoded-secret>
jwt.expiration=36000000

# JPA schema
spring.jpa.properties.hibernate.default_schema=<schema>

# OAuth2 — Google
spring.security.oauth2.client.registration.google.client-id=<id>
spring.security.oauth2.client.registration.google.client-secret=<secret>

# OAuth2 — GitHub
spring.security.oauth2.client.registration.github.client-id=<id>
spring.security.oauth2.client.registration.github.client-secret=<secret>

# Frontend redirect (used after OAuth2 success)
frontend_redirect_url=http://localhost:3000
```

> **Note:** Never commit real credentials to version control. Use environment variables or a secrets manager in production.

---

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL instance

### Build & Run

```bash
./mvnw clean package -DskipTests
java -jar target/fms-*.jar
```

The server starts on `http://localhost:8080`.

---

## Docker

### Build and run with the helper script

```bash
chmod +x run.sh
./run.sh
```

### Manual Docker commands

```bash
# Build
docker build -t fms-image .

# Run
docker run -p 8080:8080 --name fms-app fms-image
```

The Dockerfile uses a two-stage build — Maven compiles the JAR in the first stage, and a slim JRE Alpine image runs it in the second stage.

---

## CORS

The API allows requests from the following origins by default:

- `http://localhost:5173` (Vite / React dev server)
- `http://localhost:3000` (Create React App dev server)

Update `SecurityConfig.java` to add production origins.

---

## Database Schema

| Entity | Table | Key Relations |
|---|---|---|
| `UserEntity` | `user` | — |
| `GroupEntity` | `group` | `created_by → user` |
| `ItemEntity` | `item` | `group_id → group`, `created_by → user` |
| `ShareEntity` | `share` | `group_id → group`, `share_by → user`, `share_to → user` |

All entities include `createdAt` and `updatedAt` timestamps managed via JPA lifecycle hooks (`@PrePersist`, `@PreUpdate`).
