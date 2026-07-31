# Setup

## Prerequisites

- Java 21 compatible JDK.
- Maven 3.9+.
- Git.
- Docker Desktop for containerized local execution.

## Current Environment Note

The current shell does not have `java`, `mvn`, or `git` on `PATH`.

Detected local binaries:

- Maven: `C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd`
- Git: `C:\Program Files\Git\cmd\git.exe`
- Java installation directory: `C:\Program Files\Java`

PATH should be corrected before relying on normal `java`, `mvn`, and `git` commands.

The detected JDK is Java 25. The project is configured with Java 21 source compatibility, but the preferred setup is a Java 21 JDK to match the assignment exactly.

## Build

```powershell
mvn clean install
```

Fallback for this machine:

```powershell
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

## Docker

```powershell
docker compose up --build
```

The Docker setup uses the same Spring Boot application and disables the H2 console through environment configuration in `docker-compose.yml`.

## Local Database

The default profile uses file-backed H2:

```text
jdbc:h2:file:./data/url-shortener
```

The H2 console is enabled for local development at:

```text
/h2-console
```

Do not expose the H2 console in shared or production-like deployments.

Test execution uses an isolated H2 database and Flyway migrations.
