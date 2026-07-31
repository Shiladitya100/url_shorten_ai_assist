# User Manual: URL Shortener Application

This guide explains how to use the URL Shortener application from Swagger UI.

## 1. Start the application

From the project root:

```powershell
mvn spring-boot:run
```

If Maven is not available on `PATH` on this machine, use:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' spring-boot:run
```

Wait until the console shows that Spring Boot has started on port `8080`.

## 2. Open Swagger UI

Open this URL in a browser:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger shows the available API groups:

- `Security`
- `URLs`
- `Redirects`

## 3. Check application health

Open this URL in a browser:

```text
http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## 4. Get a CSRF token

The create-short-URL API is a state-changing POST endpoint, so it requires a CSRF token.

In Swagger UI:

1. Expand `Security`.
2. Expand `GET /api/v1/security/csrf`.
3. Click `Try it out`.
4. Click `Execute`.
5. Confirm the response code is `200`.
6. Copy the `token` value from the response body.

Example response:

```json
{
  "headerName": "X-XSRF-TOKEN",
  "parameterName": "_csrf",
  "token": "generated-token-value"
}
```

The response also sets an `XSRF-TOKEN` browser cookie.

## 5. Create a short URL

In Swagger UI:

1. Expand `URLs`.
2. Expand `POST /api/v1/urls`.
3. Click `Try it out`.
4. In the request body, enter:

```json
{
  "originalUrl": "https://google.com",
  "expiresAt": "2027-08-06T10:00:00Z"
}
```

5. Add a request header:

```text
X-XSRF-TOKEN: <token copied from GET /api/v1/security/csrf>
```

If Swagger shows an `X-XSRF-TOKEN` field for this operation, paste the token into that field.

6. Click `Execute`.

Expected response code:

```text
201 Created
```

Example response body:

```json
{
  "originalUrl": "https://google.com",
  "shortCode": "AbC123x",
  "shortUrl": "http://localhost:8080/AbC123x",
  "createdAt": "2026-07-31T04:30:00Z",
  "expiresAt": "2027-08-06T10:00:00Z",
  "active": true
}
```

Copy the `shortCode` value. You will need it for redirect and analytics.

If you receive `403 Forbidden`, the CSRF token header is missing, expired, or does not match the browser cookie. Run `GET /api/v1/security/csrf` again and retry the POST with the new token.

## 6. Redirect using the short code

In Swagger UI:

1. Expand `Redirects`.
2. Expand `GET /{shortCode}`.
3. Click `Try it out`.
4. Enter the `shortCode` returned by the create API.
5. Click `Execute`.

Expected response:

```text
302 Found
```

The `Location` response header contains the original URL, for example:

```text
Location: https://google.com
```

Swagger may display this as a redirect response rather than navigating automatically. You can also test redirect directly in the browser:

```text
http://localhost:8080/<shortCode>
```

Example:

```text
http://localhost:8080/AbC123x
```

## 7. View analytics

In Swagger UI:

1. Expand `URLs`.
2. Expand `GET /api/v1/urls/{shortCode}/analytics`.
3. Click `Try it out`.
4. Enter the `shortCode`.
5. Click `Execute`.

Expected response code:

```text
200 OK
```

Example response:

```json
{
  "shortCode": "AbC123x",
  "originalUrl": "https://google.com",
  "accessCount": 1,
  "createdAt": "2026-07-31T04:30:00Z",
  "expiresAt": "2027-08-06T10:00:00Z",
  "lastAccessedAt": "2026-07-31T04:35:00Z",
  "active": true,
  "expired": false
}
```

`accessCount` increases only after successful redirects.

## 8. Common validation rules

Create URL request:

- `originalUrl` is required.
- `originalUrl` must be a valid HTTPS URL.
- `originalUrl` must be at most `2048` characters.
- `expiresAt` is optional.
- `expiresAt`, when provided, must be a future timestamp.

Short code path variables:

- Must be exactly `7` characters.
- Must contain only Base62 characters:
  - `A-Z`
  - `a-z`
  - `0-9`

## 9. Common errors

| Status | Scenario | Action |
| --- | --- | --- |
| `400 Bad Request` | Invalid URL, invalid expiration, or malformed short code | Fix the request body or short code format. |
| `403 Forbidden` | Missing or invalid CSRF token on create request | Call `GET /api/v1/security/csrf`, copy the token, and send it as `X-XSRF-TOKEN`. |
| `404 Not Found` | Short code does not exist or is inactive | Verify the short code from the create response. |
| `410 Gone` | Short code exists but is expired | Create a new short URL with a future expiration. |
| `503 Service Unavailable` | Short-code generation exhausted retry attempts | Retry later; this is expected only under extreme collision/repository failure scenarios. |

## 10. End-to-end Swagger flow summary

Use this sequence:

1. Open Swagger: `http://localhost:8080/swagger-ui/index.html`
2. Execute `GET /api/v1/security/csrf`.
3. Copy the returned `token`.
4. Execute `POST /api/v1/urls` with header `X-XSRF-TOKEN`.
5. Copy the returned `shortCode`.
6. Execute `GET /{shortCode}` to test redirect.
7. Execute `GET /api/v1/urls/{shortCode}/analytics` to view access count and status.
