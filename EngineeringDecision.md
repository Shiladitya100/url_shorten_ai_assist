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

## EDR-013: Treat Expiration as a First-Class Redirect Failure

Status: Accepted

Decision:

Use a dedicated `UrlMappingExpiredException` for expired mappings and map it to `410 Gone`.

Rationale:

Expired links are different from missing or inactive links. The resource existed, but it is no longer available for redirect. `410 Gone` communicates that state more accurately than `404 Not Found`.

Trade-offs:

- Exposing `410 Gone` reveals that a short code previously existed.
- Returning `404 Not Found` for inactive links avoids exposing administrative state.
- If enumeration resistance becomes a hard security requirement, expiration responses may need to be normalized to `404 Not Found`.

## EDR-014: Expose Aggregate Analytics Only

Status: Accepted

Decision:

Expose analytics from aggregate fields on `url_mappings`: access count, last accessed timestamp, lifecycle timestamps, active status, and computed expired status.

Rationale:

The assignment requires analytics but does not require per-click attribution or event history. Aggregate analytics satisfy the initial requirement with less storage, less privacy risk, and simpler transactional behavior.

Trade-offs:

- This does not support time-series analysis, referrer tracking, geolocation, or user-agent reporting.
- Aggregate counters are easier to test and document.
- High redirect volume may require asynchronous aggregation or a separate event table later.

## EDR-015: Centralize API Exception Handling

Status: Accepted

Decision:

Use `@RestControllerAdvice` to map application and validation exceptions to a consistent `ApiErrorResponse`.

Rationale:

Centralized error handling avoids duplicated controller try/catch blocks, keeps controllers focused on HTTP routing, and gives clients a predictable error response shape.

Trade-offs:

- The global handler is Spring-specific and belongs at the application boundary.
- Error response shape is now part of the public API contract and should be versioned carefully if changed later.
- A generic catch-all handler is deferred to avoid accidentally hiding unexpected defects during development.

## EDR-016: Validate Short Codes at the API Boundary

Status: Accepted

Decision:

Require short-code path variables to match a 7-character Base62 pattern.

Rationale:

The generator produces 7-character Base62 codes. Applying the same constraint at the API boundary rejects malformed requests before they hit the service/repository layer.

Trade-offs:

- This tightly couples the public API validation contract to the current short-code length.
- If the generated length becomes configurable later, validation rules must be updated with the same source of truth.

## EDR-017: Reserve Application Route Names from Generated Codes

Status: Accepted

Decision:

Reject reserved route words during short-code generation.

Rationale:

The redirect endpoint lives at the root path. Reserved words reduce the risk of generated short links colliding with application-owned routes such as API, actuator, Swagger, or H2 console paths.

Trade-offs:

- With the current 7-character length, most reserved words are not directly generated.
- Keeping the guard now makes future code-length changes safer.

## EDR-018: Avoid Logging Full Destination URLs

Status: Accepted

Decision:

Log operational events using short code, status, count, and expiration metadata, but do not log full original URLs.

Rationale:

Original URLs can contain sensitive paths, query strings, identifiers, or tokens. Avoiding full URL logging reduces accidental data exposure while preserving useful operational observability.

Trade-offs:

- Debugging destination-specific issues may require database inspection.
- Logs remain useful for request flow, failure class, short-code lookup, and analytics counter behavior.
- Structured logging and correlation IDs are deferred to a later production-readiness milestone.

## EDR-019: Generate OpenAPI from Code Annotations

Status: Accepted

Decision:

Use Springdoc annotations on controllers and DTOs plus a small `OpenApiConfig` bean for API metadata.

Rationale:

Generated OpenAPI documentation stays close to executable controller contracts and reduces drift compared with a separate handwritten specification.

Trade-offs:

- Controller and DTO files carry documentation annotations.
- Documentation quality depends on keeping annotations current during future API changes.
- Contract-first OpenAPI generation is deferred because the current project is code-first and incrementally developed.

## EDR-020: Use Atomic Repository Update for Redirect Analytics

Status: Accepted

Decision:

Record successful redirect analytics with a repository-level update that increments `access_count` and sets `last_accessed_at`.

Rationale:

Redirect is the hot path. Updating aggregate analytics with one explicit update query avoids relying on entity dirty checking for the counter mutation and makes the increment safer when multiple redirects hit the same short code concurrently.

Trade-offs:

- The redirect flow still performs one read and one write.
- Aggregate analytics can still become a write hotspot for very popular short links.
- Event-based or asynchronous analytics would scale further but requires schema and architecture changes outside this milestone.

## EDR-021: Add Spring Security Boundary with CSRF Protection

Status: Accepted

Decision:

Use Spring Security to protect browser-facing HTTP behavior while keeping current public URL-shortener endpoints unauthenticated.

Rationale:

The create endpoint changes server state and should reject browser-originated requests without CSRF protection. Redirect and analytics reads are intentionally public in the current bearer-short-code model.

Trade-offs:

- Existing clients must send a CSRF token for `POST /api/v1/urls`.
- Authentication is not added until the domain has users, owners, or administrative operations.
- Security headers and CORS behavior are centralized and testable.

## EDR-022: Add Docker, CI, and Runbook Baseline

Status: Accepted

Decision:

Provide a Java 21 Dockerfile, Docker Compose configuration, GitHub Actions CI workflow, and runbook.

Rationale:

Build and runtime repeatability are part of production-readiness evaluation. Concrete artifacts are more valuable than documentation-only claims.

Trade-offs:

- The Docker runtime still uses H2 and is suitable for evaluation/local operation, not durable production use.
- CI runs Maven verification but does not yet publish artifacts or deploy.
