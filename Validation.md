# Validation

## Milestone 1 Validation Plan

Required checks:

- Compile project.
- Run tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review formatting/readability.
- Commit and push after successful validation.

## Milestone 1 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit tests: Passed
- Integration tests: Not applicable yet; no integration tests exist in Milestone 1
- Static analysis: Not configured yet
- Formatting review: Basic file structure and readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced

Test summary:

- Tests run: 1
- Failures: 0
- Errors: 0
- Skipped: 0

Notes:

- The current shell does not expose `java`, `mvn`, or `git` on `PATH`.
- Validation used absolute tool paths.
- Runtime detected during validation was Java 25.0.3 while Maven compiles with Java release target 21.
- Lombok and Mockito emitted Java 25-related warnings. These are not build failures but should be monitored. Using a Java 21 runtime for local validation would better match the assignment.

## Milestone 2 Validation Plan

Required checks:

- Compile project.
- Run repository integration test.
- Confirm Flyway migration applies.
- Confirm Hibernate schema validation passes.
- Execute `mvn clean install`.
- Review for secrets.
- Commit and push after successful validation.

## Milestone 2 Validation Results

Initial check:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean test
```

Result:

- Build: Passed
- Flyway migrations applied: 1
- Hibernate schema validation: Passed
- Tests run: 2
- Failures: 0
- Errors: 0
- Skipped: 0

Final `mvn clean install` result:

- Build: Passed
- Compilation: Passed
- Tests run: 2
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known from Milestone 1 and remain non-blocking build warnings.

## Milestone 3 Validation Plan

Required checks:

- Compile project.
- Run domain model tests.
- Run mapper tests.
- Run repository regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Commit and push after successful validation.

## Milestone 3 Validation Results

Initial check:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean test
```

Result:

- Build: Passed
- Tests run: 7
- Failures: 0
- Errors: 0
- Skipped: 0

Final `mvn clean install` result:

- Build: Passed
- Compilation: Passed
- Tests run: 7
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 5 Validation Plan

Required checks:

- Compile project.
- Run create service unit test.
- Run create controller test.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review API validation behavior.
- Commit and push after successful validation.

## Milestone 5 Validation Results

Review completion check on 2026-07-30:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 16
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- API validation review: Passed; invalid URLs and past expiration timestamps are rejected

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are non-blocking in the current environment but should be revisited with a Java 21 runtime.

Initial check:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean test
```

Result:

- Build: Passed
- Tests run: 15
- Failures: 0
- Errors: 0
- Skipped: 0

Final `mvn clean install` result:

- Build: Passed
- Compilation: Passed
- Tests run: 15
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 4 Validation Plan

Required checks:

- Compile project.
- Run Base62 generator tests.
- Run unique generation service tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets and unsafe randomness.
- Commit and push after successful validation.

## Milestone 4 Validation Results

Initial check:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean test
```

Result:

- Build: Passed
- Tests run: 12
- Failures: 0
- Errors: 0
- Skipped: 0

Final `mvn clean install` result:

- Build: Passed
- Compilation: Passed
- Tests run: 12
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced; generation uses `SecureRandom`

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 6 Validation Plan

Required checks:

- Compile project.
- Run redirect controller tests.
- Run redirect service tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review redirect status-code behavior.
- Commit and push after successful validation.

## Milestone 6 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 22
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Redirect behavior review: Passed; success returns `302 Found`, missing returns `404 Not Found`, expired returns `410 Gone`

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 7 Validation Plan

Required checks:

- Compile project.
- Run expiration-focused service tests.
- Run redirect controller status-code tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review expiration boundary behavior.
- Commit and push after successful validation.

## Milestone 7 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 25
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Expiration review: Passed; `expiresAt == now` is expired, expired redirects return `410 Gone`, inactive redirects return `404 Not Found`

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 8 Validation Plan

Required checks:

- Compile project.
- Run analytics controller tests.
- Run analytics service tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review analytics privacy and limitation documentation.
- Commit and push after successful validation.

## Milestone 8 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 30
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Analytics review: Passed; endpoint exposes aggregate-only statistics and documents limitations

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 9 Validation Plan

Required checks:

- Compile project.
- Run controller tests for global error response behavior.
- Run existing service, repository, mapper, and utility regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review public error response shape.
- Commit and push after successful validation.

## Milestone 9 Validation Results

Executed on: 2026-07-30

Attempt 1:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Failed
- Cause: `@WebMvcTest` controller slices did not provide a `Clock` bean required by `GlobalExceptionHandler`.
- Fix: Use `Clock.systemUTC()` inside the global handler because tests do not assert exact error timestamps and the handler does not need business-time control.

Attempt 2:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 31
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Error response review: Passed; controllers now use centralized exception handling and return consistent error bodies

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 10 Validation Plan

Required checks:

- Compile project.
- Run controller tests for malformed path-variable validation.
- Run short-code generation tests for reserved-code retry behavior.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review validation rules and route-collision risks.
- Commit and push after successful validation.

## Milestone 10 Validation Results

Executed on: 2026-07-30

Attempt 1:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Failed
- Cause: Existing inactive-link controller test used `inactive`, which violates the new 7-character Base62 short-code rule.
- Fix: Updated the test fixture to use valid short code `InActv1`.

Attempt 2:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 34
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Validation review: Passed; malformed short-code paths return `400 Bad Request`, reserved generated candidates are retried

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 11 Validation Plan

Required checks:

- Compile project.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review logs for sensitive-data exposure.
- Confirm full original URLs are not logged.
- Review logging level defaults.
- Commit and push after successful validation.

## Milestone 11 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 34
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Logging review: Passed; application logs use short codes and operational metadata, not full original URLs

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 12 Validation Plan

Required checks:

- Compile project.
- Run existing regression tests.
- Execute `mvn clean install`.
- Confirm Springdoc annotations compile.
- Review Swagger UI/OpenAPI endpoint documentation.
- Review for secrets.
- Commit and push after successful validation.

## Milestone 12 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 34
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- OpenAPI review: Passed; Springdoc annotations compile and Swagger/OpenAPI endpoints are documented

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 13 Status

Docker milestone was skipped by engineer instruction.

## Milestone 14 Validation Plan

Required checks:

- Compile project.
- Run newly added unit tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review that no production behavior changed.
- Commit and push after successful validation.

## Milestone 14 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 42
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Production behavior review: Passed; milestone changes are test and documentation only

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 15 Status

Integration Tests milestone was skipped by engineer instruction.

## Milestone 16 Validation Plan

Required checks:

- Compile project.
- Run repository regression test for atomic successful-access update.
- Run redirect service regression tests.
- Run existing regression tests.
- Execute `mvn clean install`.
- Review for secrets.
- Review performance trade-offs and rollback approach.
- Commit and push after successful validation.

## Milestone 16 Validation Results

Executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 43
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Basic readability reviewed
- Basic security review: Passed; no secrets, credentials, or hardcoded passwords introduced
- Performance review: Passed; redirect analytics now use an atomic repository update

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.

## Milestone 17 Status

Security Improvements milestone was skipped by engineer instruction.

Documentation-only validation executed on: 2026-07-30

Command:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.3'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\apache-maven-3.9.16\bin\mvn.cmd' clean install
```

Result:

- Build: Passed
- Compilation: Passed
- Unit/controller/repository tests: Passed
- Tests run: 43
- Failures: 0
- Errors: 0
- Skipped: 0
- Package/install: Passed
- Static analysis: Not configured yet
- Formatting review: Documentation readability reviewed
- Basic security review: Passed; no production code or configuration changed

Notes:

- Validation used Java 25.0.3 runtime with Java 21 release target because Java 21 is not currently detected.
- Lombok and Mockito emitted Java 25-related warnings. These are known non-blocking build warnings in the current environment.
