# API Documentation

API implementation begins in later milestones.

Persistence support added in Milestone 2:

- URL mappings are stored in the `url_mappings` table.
- `short_code` is unique.
- Aggregate analytics fields are present but not exposed yet.

Planned APIs:

- `POST /api/v1/urls` — create short URL
- `GET /{shortCode}` — redirect to original URL
- `GET /api/v1/urls/{shortCode}/analytics` — retrieve analytics
- `GET /actuator/health` — health check

OpenAPI/Swagger will be configured in a dedicated milestone.
