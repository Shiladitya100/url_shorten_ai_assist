# ADR-001: Security Boundary for Public URL Shortener APIs

Status: Accepted

## Context

The application exposes a public redirect endpoint and a state-changing create endpoint. CSRF coverage was identified as an improvement area.

## Decision

Use Spring Security as the HTTP security boundary.

- Keep redirect and analytics GET endpoints public.
- Require CSRF tokens for `POST /api/v1/urls`.
- Enable standard security headers.
- Configure CORS through application properties.
- Keep authentication out of scope until user ownership or administrative operations are introduced.

## Consequences

- Browser clients must send a CSRF token for create requests.
- API behavior is more explicit and testable.
- The project avoids introducing fake authentication that is not backed by a user model.
