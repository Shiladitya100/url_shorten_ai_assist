# Testing Strategy

## Test Levels

- Unit tests for service, utility, mapper, validation, and exception logic.
- Controller tests for API contracts and error handling.
- Integration tests for database-backed flows.

## Current Tests

Milestone 1 includes a Spring context-load test to verify the application bootstrap.

Milestone 2 adds a repository integration test for:

- Flyway schema creation.
- JPA entity persistence.
- `existsByShortCode`.
- `findByShortCode`.

Milestone 3 adds unit tests for:

- Expiration behavior.
- Redirectability behavior.
- Access recording.
- Mapping to short URL response.
- Mapping to analytics response.

Milestone 4 adds unit tests for:

- Base62 character generation.
- Invalid generator length handling.
- Unique candidate acceptance.
- Collision retry behavior.
- Exhausted generation attempts.

Milestone 5 adds tests for:

- Create URL service orchestration.
- Persistence object construction.
- Short URL construction from configured base URL.
- Create endpoint `201 Created` response.
- Invalid URL request rejection.
- Past expiration request rejection.

Milestone 6 adds tests for:

- Redirect endpoint `302 Found` response with `Location` header.
- Missing short-code `404 Not Found` response.
- Expired short-code `410 Gone` response.
- Redirect service lookup behavior.
- Access count and last accessed timestamp mutation.
- Regression coverage that expired mappings do not record access.

Milestone 7 adds tests for:

- Dedicated expired mapping exception behavior.
- Expiration boundary where `expiresAt == now` is expired.
- Inactive mappings return not found behavior instead of gone.
- Regression coverage that inactive mappings do not record access.

Milestone 8 adds tests for:

- Analytics endpoint response contract.
- Missing analytics short-code `404 Not Found` response.
- Analytics service lookup and mapping behavior.
- Expired flag computation in analytics responses.

Milestone 9 adds tests for:

- Standard validation error response shape.
- Standard missing short-code error response shape.
- Standard expired short-code error response shape.
- Standard inactive short-code error response shape.
- Short-code generation failure mapped to `503 Service Unavailable`.

Milestone 10 adds tests for:

- Malformed redirect short-code validation.
- Malformed analytics short-code validation.
- Reserved generated short-code retry behavior.

Milestone 11 logging validation is primarily review-based:

- Confirm logging does not expose full original URLs.
- Confirm create, redirect, analytics, validation, and handled error flows have useful log events.
- Run existing regression tests to ensure logging changes do not alter behavior.

Milestone 12 OpenAPI validation is primarily build and startup based:

- Confirm Springdoc dependency compiles with controller and DTO annotations.
- Run existing regression tests to ensure annotations do not alter API behavior.
- Document Swagger UI and OpenAPI JSON endpoints.

Milestone 13 Docker was skipped by engineer decision.

Milestone 14 adds focused unit tests for:

- Shared short-code validation rules.
- Reserved short-code detection.
- Public short URL construction from configured base URL.
- OpenAPI metadata configuration.
- Application exception message contracts.

Milestone 15 Integration Tests was skipped by engineer decision.

Milestone 16 adds performance regression coverage for:

- Repository-level atomic successful-access update.
- Redirect service using repository update instead of entity-local analytics mutation.
- Expired and inactive redirects not recording access.
