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
