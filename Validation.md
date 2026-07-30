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
