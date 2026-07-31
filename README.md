# URL Shortener

Production-grade URL shortener built with Java 21, Spring Boot 3.x, Maven, H2, Spring Data JPA, Flyway, validation, OpenAPI, Docker, and automated tests.

## Current Status

Milestone 18: Security and Operability Improvements.

The project currently contains the Spring Boot skeleton, package structure, baseline dependencies, H2/Flyway configuration, URL mapping persistence, domain behavior, DTOs, mapper, short-code generation, the create-short-URL API, the redirect API, explicit expiration handling, aggregate analytics, centralized API exception handling, hardened request validation, scoped application logging, Swagger/OpenAPI documentation, expanded unit test coverage, a low-risk redirect analytics update optimization, Spring Security CSRF/header/CORS hardening, Docker packaging, Docker Compose, CI workflow, ADRs, and an operator runbook.

## Local Build

```powershell
mvn clean install
```

If Maven is not on `PATH`, use the installed Maven binary directly.

```powershell
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

## Docker

```powershell
docker compose up --build
```

Health check:

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

## Documentation

- [Architecture.md](Architecture.md)
- [API.md](API.md)
- [EngineeringDecision.md](EngineeringDecision.md)
- [RiskAnalysis.md](RiskAnalysis.md)
- [Validation.md](Validation.md)
- [TestingStrategy.md](TestingStrategy.md)
- [ReleaseNotes.md](ReleaseNotes.md)
- [Setup.md](Setup.md)
- [user-manual.md](user-manual.md)
- [AssumptionsAndConstraints.md](AssumptionsAndConstraints.md)
- [Runbook.md](Runbook.md)
- [adr/ADR-001-security-boundary.md](adr/ADR-001-security-boundary.md)
- [adr/ADR-002-operability-and-deployment.md](adr/ADR-002-operability-and-deployment.md)
- [AIEngineeringLog.md](AIEngineeringLog.md)

## Security Note

`POST /api/v1/urls` is CSRF-protected. Clients can call `GET /api/v1/security/csrf` to receive the token and `XSRF-TOKEN` cookie, then send the token in the `X-XSRF-TOKEN` header. Public GET endpoints remain unauthenticated by design.

## Swagger / OpenAPI

When the application is running locally:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
