# Release Notes

## Unreleased

### Added

- Spring Boot Maven project skeleton.
- Required package structure.
- Baseline documentation files.
- Initial context-load test.
- H2 datasource configuration.
- Flyway V1 migration for `url_mappings`.
- `UrlMapping` JPA entity.
- `UrlMappingRepository`.
- Repository integration test.
- Domain behavior for expiration, redirectability, and access recording.
- DTOs for create, short URL response, and analytics response.
- Hand-written URL mapping mapper.
- Domain and mapper unit tests.
- Base62 short-code generator.
- Unique short-code generation service.
- Bounded collision retry handling.
- Short-code generation tests.
- Create short URL service.
- Create short URL controller endpoint.
- Configurable public base URL.
- HTTPS URL validation for create requests.
- Create API tests.
- Future expiration validation for create requests.
- Redirect endpoint for short codes.
- Redirect access-count and last-accessed timestamp updates.
- Redirect controller and service tests.
- Explicit expired-link handling with `410 Gone`.
- Expiration boundary tests for redirects.
- Analytics endpoint for short-code aggregate statistics.
- Analytics service and controller tests.
- Global exception handler with consistent API error response body.
- Standardized validation, not-found, expired, inactive, and generation-failure error handling.
- Short-code path-variable validation.
- Reserved route-name protection during short-code generation.
- Scoped application logging for create, redirect, analytics, validation, and handled error flows.
- Logging policy to avoid full original URL exposure.
- OpenAPI metadata configuration.
- Swagger annotations for create, redirect, analytics, and error response models.
- Focused unit tests for validation rules, URL-shortener configuration, OpenAPI metadata, and exception message contracts.
- Atomic repository update for successful redirect analytics.

## Security and Operability Improvements

- Added Spring Security boundary with CSRF protection for `POST /api/v1/urls`.
- Added `GET /api/v1/security/csrf` for browser and manual API clients.
- Kept public redirect and analytics GET endpoints unauthenticated.
- Added CORS configuration and standard security headers.
- Added Dockerfile, Docker Compose, `.dockerignore`, and GitHub Actions CI workflow.
- Added assumptions/constraints traceability, ADRs, and runbook documentation.
- Added security-focused MockMvc tests.
