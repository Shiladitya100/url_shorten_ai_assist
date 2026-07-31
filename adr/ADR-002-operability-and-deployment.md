# ADR-002: Operability and Deployment Baseline

Status: Accepted

## Context

The application had local Maven validation but lacked concrete deployment artifacts and an operator runbook.

## Decision

Provide a baseline deployment and operability package:

- Multi-stage Dockerfile using Java 21.
- Docker Compose for local container startup.
- GitHub Actions CI workflow running `mvn clean verify` on Java 21.
- Runbook with startup, health-check, security, and troubleshooting steps.

## Consequences

- Engineers can reproduce build and runtime checks.
- Docker deployment remains local-development grade because H2 is still the persistence engine.
- Production deployment would still require externalized durable database configuration.
