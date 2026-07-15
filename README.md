# Authorization Server

A modern Authorization Server implementing **OAuth 2.0** and **OpenID Connect (OIDC)** using Spring Authorization Server.

The project provides a complete authentication and authorization solution with user management, OAuth2 client management, social login, consent management and an administrative panel.

---

## Features

### Authentication

- Email/password authentication
- User registration
- Email verification
- Password reset
- Session management
- Device management

### OAuth2 & OpenID Connect

- Authorization Code Flow
- PKCE
- Refresh Token
- Access Token
- ID Token
- OAuth2 Consent Screen
- Token Revocation
- OpenID Connect Discovery Endpoint
- JWK Set Endpoint

### Social Login

- Google

### Administration

- User management
- Role management
- Authorities management
- OAuth2 client management

### User Cabinet

- Profile editing
- Password change
- Active sessions
- Authorized applications

---

# Tech Stack

### Backend

- Java
- Maven
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Authorization Server
- Spring OAuth2 client
- PostgreSQL
- Redis
- Liquibase

### Frontend

- TypeScript
- React
- Vite

### DevOps

- Docker
- Docker Compose
- Git

---

# OAuth2 Flow

```
User
 │
 │ Login
 ▼
Authorization Server
 │
 │ Authorization Code
 ▼
OAuth2 Client
 │
 │ Exchange Code
 ▼
Authorization Server
 │
 │
 ├── Access Token
 ├── Refresh Token
 └── ID Token

```

# Running locally

## Requirements

- Java 21
- Node.js 20+
- Docker

Clone repository

```bash
git clone https://github.com/NikitaKirilov/AuthorizationServer.git
```

Start infrastructure

```bash
cd docker
docker compose up
```

Run backend application

```bash
cd backend
./mvnw spring-boot:run
```

Run frontend application

```bash
cd frontend
npm install
npm run dev
```

# Repository Structure

```
backend/
frontend/
client-example/
docker/
```

# License

Code released under the [MIT License](LICENSE)
