# Runbook

## Local startup

```powershell
mvn spring-boot:run
```

Fallback on the current workstation:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' spring-boot:run
```

## Docker startup

```powershell
docker compose up --build
```

The application listens on:

```text
http://localhost:8080
```

## Health checks

```powershell
Invoke-RestMethod http://localhost:8080/actuator/health
```

Expected status:

```json
{
  "status": "UP"
}
```

## CSRF-protected create flow

`POST /api/v1/urls` requires a CSRF token. Browser clients should first obtain the `XSRF-TOKEN` cookie and send it back in the `X-XSRF-TOKEN` header.

PowerShell curl sequence:

```powershell
$csrf = curl.exe -s -c cookies.txt http://localhost:8080/api/v1/security/csrf | ConvertFrom-Json
curl.exe -X POST `
  -b cookies.txt `
  -H "accept: */*" `
  -H "Content-Type: application/json" `
  -H "X-XSRF-TOKEN: $($csrf.token)" `
  -d '{"originalUrl":"https://google.com","expiresAt":"2027-08-06T10:00:00Z"}' `
  http://localhost:8080/api/v1/urls
```

GET endpoints remain public:

- `GET /api/v1/security/csrf`
- `GET /{shortCode}`
- `GET /api/v1/urls/{shortCode}/analytics`
- `GET /actuator/health`

## Operational checks

- Confirm the configured public base URL:
  - `APP_URL_SHORTENER_BASE_URL`
  - `app.url-shortener.base-url`
- Confirm allowed CORS origins:
  - `APP_SECURITY_CORS_ALLOWED_ORIGINS`
  - `app.security.cors.allowed-origins`
- Confirm H2 console is disabled outside local development:
  - `SPRING_H2_CONSOLE_ENABLED=false`

## Common failures

| Symptom | Likely cause | Action |
| --- | --- | --- |
| `403 Forbidden` on create | Missing CSRF token | Send `X-XSRF-TOKEN` matching the `XSRF-TOKEN` cookie. |
| `400 Bad Request` on create | Invalid URL or past expiration | Use HTTPS URL and future `expiresAt`. |
| `404 Not Found` on redirect | Missing or inactive short code | Verify the short code exists and is active. |
| `410 Gone` on redirect | Short code expired | Create a new short URL or extend expiration through a future admin feature. |
| Application fails schema validation | Flyway migration/entity mismatch | Add a migration or fix entity mapping. |

## Rollback

This workspace does not perform automatic commits. To roll back local changes, review `git status` and manually revert the specific files you do not want to keep.
