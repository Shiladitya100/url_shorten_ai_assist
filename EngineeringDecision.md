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
