# API Documentation

API implementation begins in later milestones.

Persistence support added in Milestone 2:

- URL mappings are stored in the `url_mappings` table.
- `short_code` is unique.
- Aggregate analytics fields are present but not exposed yet.

Domain API models added in Milestone 3:

- `CreateShortUrlRequest`
- `ShortUrlResponse`
- `UrlAnalyticsResponse`

Planned APIs:

- `POST /api/v1/urls` — create short URL
- `GET /{shortCode}` — redirect to original URL
- `GET /api/v1/urls/{shortCode}/analytics` — retrieve analytics
- `GET /actuator/health` — health check

## Create Short URL

```http
POST /api/v1/urls
Content-Type: application/json
```

Request:

```json
{
  "originalUrl": "https://example.com/articles/123",
  "expiresAt": "2026-08-06T10:00:00Z"
}
```

Response:

```http
201 Created
Location: http://localhost:8080/AbC123x
```

```json
{
  "originalUrl": "https://example.com/articles/123",
  "shortCode": "AbC123x",
  "shortUrl": "http://localhost:8080/AbC123x",
  "createdAt": "2026-07-30T10:00:00Z",
  "expiresAt": "2026-08-06T10:00:00Z",
  "active": true
}
```

Current validation:

- `originalUrl` is required.
- `originalUrl` must be at most 2048 characters.
- `originalUrl` must be a valid HTTPS URL.
- `expiresAt` is optional.

OpenAPI/Swagger will be configured in a dedicated milestone.
