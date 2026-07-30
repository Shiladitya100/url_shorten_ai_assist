# API Documentation

## Implemented APIs

### Create Short URL

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

Validation:

- `originalUrl` is required.
- `originalUrl` must be at most 2048 characters.
- `originalUrl` must be a valid HTTPS URL.
- `expiresAt` is optional.
- `expiresAt`, when provided, must be in the future.
- `shortCode` path variables must be 7-character Base62 values.

### Redirect URL

```http
GET /{shortCode}
```

Successful response:

```http
302 Found
Location: https://example.com/articles/123
```

Error responses:

- `404 Not Found` when the short code does not exist.
- `404 Not Found` when the short code is inactive.
- `410 Gone` when the short code is expired.

Side effects:

- Successful redirects increment `access_count`.
- Successful redirects update `last_accessed_at`.

### URL Analytics

```http
GET /api/v1/urls/{shortCode}/analytics
```

Successful response:

```json
{
  "shortCode": "AbC123x",
  "originalUrl": "https://example.com/articles/123",
  "accessCount": 5,
  "createdAt": "2026-07-29T10:00:00Z",
  "expiresAt": "2026-08-06T10:00:00Z",
  "lastAccessedAt": "2026-07-30T09:30:00Z",
  "active": true,
  "expired": false
}
```

Error responses:

- `404 Not Found` when the short code does not exist.

Limitations:

- Analytics are aggregate only.
- No per-click event history is stored.
- Only successful redirects update `accessCount` and `lastAccessedAt`.

## Planned APIs

- `GET /actuator/health` - health check.

OpenAPI/Swagger will be configured in a dedicated milestone.

## Error Response

API errors use a consistent response body:

```json
{
  "timestamp": "2026-07-30T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/v1/urls",
  "fieldErrors": [
    {
      "field": "originalUrl",
      "message": "must be a valid URL"
    }
  ]
}
```

Status mapping:

- Validation failure: `400 Bad Request`
- Missing short code: `404 Not Found`
- Inactive short code: `404 Not Found`
- Expired short code: `410 Gone`
- Short-code generation exhaustion: `503 Service Unavailable`
