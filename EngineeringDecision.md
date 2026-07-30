# Engineering Decision Records

## EDR-001: Bootstrap with Spring Boot Maven Project

Status: Accepted

Decision:

Use a standard Spring Boot 3.x Maven project with Java 21 source compatibility.

Rationale:

This matches the assignment stack and provides a stable foundation for web APIs, validation, persistence, health checks, testing, and packaging.

Trade-offs:

- Spring Boot accelerates implementation but introduces framework conventions that must be kept disciplined.
- Maven is explicit and evaluation-friendly, but less concise than Gradle.

Alternatives considered:

- Gradle: rejected because Maven is explicitly required.
- Manual Java application: rejected because Spring Boot is explicitly required.

## EDR-002: Use Flyway-Managed Schema with Hibernate Validation

Status: Accepted

Decision:

Use Flyway migrations as the source of truth for database schema and configure Hibernate with `ddl-auto: validate`.

Rationale:

Flyway provides explicit, reviewable schema changes. Hibernate validation catches mismatches between JPA entities and the actual database schema without allowing Hibernate to mutate schema implicitly.

Benefits:

- Safer database evolution.
- Reproducible local and test schema.
- Clear audit trail for database changes.

Trade-offs:

- Every schema change requires a migration.
- Entity changes can fail startup until the matching migration is added.

Alternatives considered:

- `ddl-auto: update`: rejected because it hides schema changes and is unsuitable for production discipline.
- `ddl-auto: create-drop`: useful for tests, but rejected for application configuration because it does not model production schema management.

## EDR-003: Store Initial Analytics Aggregates on URL Mapping

Status: Accepted

Decision:

Store `access_count` and `last_accessed_at` directly on `url_mappings` for the first implementation.

Rationale:

The assignment requires analytics, but does not require event-level analytics. Aggregate fields satisfy the initial requirement with lower schema and privacy complexity.

Trade-offs:

- Simple and efficient for basic analytics.
- Does not preserve per-click history.
- High-write redirect traffic may eventually require a separate analytics event table or asynchronous aggregation.

## EDR-004: Use Java Records for DTOs

Status: Accepted

Decision:

Use Java records for request and response DTOs.

Rationale:

DTOs should be immutable transport models. Records reduce boilerplate while keeping the API model explicit and readable.

Trade-offs:

- Records are less flexible for frameworks that require no-argument constructors, but Spring MVC and Jackson support records.
- Validation annotations must be placed directly on record components.

Alternatives considered:

- Lombok DTO classes: rejected for now because records are simpler for immutable API models.

## EDR-005: Use Hand-Written Mapper Initially

Status: Accepted

Decision:

Use a small hand-written `UrlMappingMapper` instead of introducing MapStruct.

Rationale:

The current mapping logic is minimal. A hand-written mapper avoids extra annotation processing and keeps behavior transparent.

Trade-offs:

- Manual mapping can become repetitive as DTOs grow.
- MapStruct may be reconsidered if mapping complexity increases.

## EDR-006: Use SecureRandom Base62 Short Codes

Status: Accepted

Decision:

Generate 7-character short codes using a Base62 alphabet and `SecureRandom`.

Rationale:

Base62 produces URL-safe, compact, human-readable codes. `SecureRandom` avoids predictable sequences and is a safer default for public identifiers than sequential IDs or standard pseudo-random generators.

Benefits:

- URL-safe output.
- Large initial keyspace.
- Avoids exposing creation volume.

Trade-offs:

- Random generation requires collision checks.
- Codes are not ordered by creation time.

Alternatives considered:

- Sequential numeric IDs: rejected because they expose usage volume and are easier to enumerate.
- UUIDs: rejected because they are too long for a shortener.

## EDR-007: Bound Collision Retry Attempts

Status: Accepted

Decision:

Limit unique short-code generation to 10 attempts before failing with `ShortCodeGenerationException`.

Rationale:

Generation should not loop indefinitely under unexpected collision or repository behavior.

Trade-offs:

- Extremely unlikely collision bursts could fail a request.
- Bounded failure is preferable to hanging a request thread.

## EDR-008: Require HTTPS Original URLs for Create API

Status: Accepted

Decision:

Require `originalUrl` to be a valid HTTPS URL for the create endpoint.

Rationale:

HTTPS-only URLs are a safer default for a public shortener and avoid promoting insecure redirects.

Trade-offs:

- HTTP URLs are rejected even if valid.
- Some internal or local testing URLs require future profile-specific relaxation if needed.

Alternatives considered:

- Allow both HTTP and HTTPS: deferred until a concrete business need exists.

## EDR-009: Build Short URLs from Application Configuration

Status: Accepted

Decision:

Use `app.url-shortener.base-url` to build public short URLs.

Rationale:

The externally visible URL should not be hardcoded inside service logic. Configuration allows local, Docker, and deployed environments to use different base URLs.

Trade-offs:

- Incorrect configuration can produce invalid public URLs.
- Future production deployment should validate this setting more strictly.

## EDR-010: Reject Past Expiration Timestamps at the API Boundary

Status: Accepted

Decision:

Use Bean Validation `@Future` on `CreateShortUrlRequest.expiresAt`.

Rationale:

An expiration timestamp in the past creates a short URL that is immediately unusable. Rejecting this at the request boundary keeps invalid state out of the service and database.

Trade-offs:

- Standard Bean Validation keeps the implementation lightweight.
- Time-based validation depends on the application clock used by the validation framework.
- More complex expiration rules, such as minimum TTL or maximum TTL, are deferred until there is a concrete business requirement.

## EDR-011: Use 302 Found for Redirects

Status: Accepted

Decision:

Return `302 Found` for successful short-code redirects.

Rationale:

Short URLs may need future policy changes, expiration checks, analytics updates, or destination edits. A temporary redirect avoids telling clients and intermediaries to permanently cache the destination.

Trade-offs:

- `302 Found` may produce slightly more repeat traffic than a permanent redirect.
- `301 Moved Permanently` could be more cache-friendly, but it is less flexible for mutable short-link behavior.

## EDR-012: Keep Redirect Exceptions Framework-Free

Status: Accepted

Decision:

Service-layer redirect failures use application exceptions. The controller translates those exceptions to HTTP statuses until global exception handling is implemented.

Rationale:

This keeps the service layer independent from Spring MVC and preserves the clean architecture boundary.

Trade-offs:

- Controller-level exception translation is acceptable for one endpoint.
- A global exception handler should replace local translation as the API surface grows.
