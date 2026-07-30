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
