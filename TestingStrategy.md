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
