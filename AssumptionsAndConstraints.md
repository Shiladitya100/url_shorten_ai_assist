# Assumptions and Constraints

## Assumptions

| ID | Assumption | Impact | Validation |
| --- | --- | --- | --- |
| A-001 | The service is a single-tenant evaluation application, not a multi-user SaaS platform. | No owner model or per-user authorization is implemented. | Public create, redirect, and analytics APIs are documented explicitly. |
| A-002 | Short URLs are public bearer-style resources. Anyone with a valid short code can redirect and read aggregate analytics. | Analytics are not treated as private user-owned data. | API docs and risk analysis call out this exposure. |
| A-003 | Original URLs must use HTTPS. | HTTP and local-network URLs are rejected. | Bean Validation and controller tests enforce HTTPS-only input. |
| A-004 | H2 is acceptable for local/evaluation persistence. | Production database concerns are deferred. | Flyway and repository boundaries keep migration to another database feasible. |
| A-005 | Aggregate analytics are sufficient for the assignment scope. | No per-click event table, referrer, IP, or user-agent tracking is stored. | Analytics tests cover aggregate counter behavior. |

## Constraints

| ID | Constraint | Current response |
| --- | --- | --- |
| C-001 | Java target is 21. | Maven compiler release is set to Java 21; CI uses Temurin 21. |
| C-002 | State-changing browser-style requests need CSRF protection. | Spring Security requires CSRF tokens for `POST /api/v1/urls`. |
| C-003 | Public redirect endpoint must remain unauthenticated. | `GET /{shortCode}` remains publicly accessible. |
| C-004 | Secrets must not be hardcoded. | No passwords or tokens are committed; runtime config is externalized. |
| C-005 | Local H2 console is development-only. | Docker Compose disables the H2 console by environment variable. |

## Traceability

| Requirement | Implementation | Tests / validation |
| --- | --- | --- |
| Create short URL | `UrlController`, `UrlShorteningServiceImpl`, `UrlMappingRepository` | `UrlControllerTest`, `UrlShorteningServiceImplTest` |
| Redirect short URL | `RedirectController`, `UrlShorteningServiceImpl` | `RedirectControllerTest`, service tests |
| Expiration | `CreateShortUrlRequest`, `UrlMapping`, service checks | controller and entity tests |
| Aggregate analytics | `UrlAnalyticsResponse`, mapper, repository counter update | controller, mapper, repository, service tests |
| Security boundary | `SecurityConfig` | `SecurityConfigTest`, controller tests |
| Deployment repeatability | `Dockerfile`, `docker-compose.yml`, CI workflow | Maven validation; Docker build can be run locally |
