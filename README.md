# Task Tracking System

A RESTful task-management backend built with Java 17, Spring Boot 3, Spring Security, JWT, JPA, and H2. It supports accounts, teams, task assignment and filtering, comments, attachments, notifications, and a local AI-description helper.

## Features

- BCrypt password hashing and JWT authentication
- User registration, login, logout guidance, and profile management
- Teams/projects, membership, and owner/admin invitations
- Task CRUD, completion, due dates, priorities, assignment, filtering, sorting, and search
- Task comments and uploaded attachments
- Stored and Server-Sent Event (SSE) notifications
- Optional local task-description generator endpoint
- Swagger/OpenAPI documentation and H2 console

## Requirements

- JDK 17+
- Apache Maven 3.9+

## Run

```powershell
mvn spring-boot:run
```

API base URL: `http://localhost:8080`  
Swagger: `http://localhost:8080/swagger-ui.html`  
H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:tasktracker`)

For production, set `JWT_SECRET` to a secret of at least 32 bytes. Set `UPLOAD_DIR` to a persistent storage directory.

## Main API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create account |
| POST | `/api/auth/login` | Obtain JWT |
| POST | `/api/auth/logout` | Logout instruction; discard JWT client-side |
| GET/PUT | `/api/users/me` | View/update profile |
| POST/GET | `/api/teams` | Create/list teams |
| GET | `/api/teams/{id}` | Team details |
| POST | `/api/teams/{id}/members` | Invite member by email |
| POST | `/api/teams/{teamId}/tasks` | Create task |
| GET | `/api/tasks` | Filter/search tasks (`teamId`, `assigneeId`, `status`, `search`, `sort`) |
| GET | `/api/tasks/assigned-to-me` | Tasks assigned to current user |
| GET/PUT/DELETE | `/api/tasks/{id}` | Read/update/delete task |
| PATCH | `/api/tasks/{id}/complete` | Mark task completed |
| GET/POST | `/api/tasks/{id}/comments` | Read/add comments |
| GET/POST | `/api/tasks/{id}/attachments` | List/upload attachments |
| GET | `/api/attachments/{id}/download` | Download attachment |
| GET | `/api/notifications` | Stored notifications |
| GET | `/api/notifications/stream` | SSE notification stream |
| POST | `/api/ai/tasks/generate-description` | Generate structured description locally |

Send `Authorization: Bearer <token>` to protected endpoints.

## Example

```json
POST /api/auth/register
{"name":"Demo User","email":"demo@example.com","password":"Password123"}

POST /api/teams
Authorization: Bearer <token>
{"name":"Platform Team","description":"Core platform work"}

POST /api/teams/1/tasks
Authorization: Bearer <token>
{"title":"Build login","description":"Implement secure login","status":"OPEN","priority":"HIGH","dueDate":"2026-12-15","assigneeId":1}
```

## Tests

```powershell
mvn test
```

## Database

H2 is the default development database and resets when the application stops. For persistent deployment, replace datasource settings in `application.yml` with PostgreSQL/MySQL and add the corresponding JDBC driver.

## Submission

Create a public GitHub repository, commit all project files, push the repository, and submit the repository URL.
