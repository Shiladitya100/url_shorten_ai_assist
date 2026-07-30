# URL Shortener

Production-grade URL shortener built with Java 21, Spring Boot 3.x, Maven, H2, Spring Data JPA, Flyway, validation, OpenAPI, Docker, and automated tests.

## Current Status

Milestone 9: Exception Handling.

The project currently contains the Spring Boot skeleton, package structure, baseline dependencies, H2/Flyway configuration, URL mapping persistence, domain behavior, DTOs, mapper, short-code generation, the create-short-URL API, the redirect API, explicit expiration handling, aggregate analytics, and centralized API exception handling.

## Local Build

```powershell
mvn clean install
```

If Maven is not on `PATH`, use the installed Maven binary directly.

```powershell
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

## Documentation

- [Architecture.md](Architecture.md)
- [API.md](API.md)
- [EngineeringDecision.md](EngineeringDecision.md)
- [RiskAnalysis.md](RiskAnalysis.md)
- [Validation.md](Validation.md)
- [TestingStrategy.md](TestingStrategy.md)
- [ReleaseNotes.md](ReleaseNotes.md)
- [Setup.md](Setup.md)
- [AIEngineeringLog.md](AIEngineeringLog.md)
