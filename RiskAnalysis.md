# Risk Analysis

## Current Risks

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Java, Maven, and Git are not on PATH | Build and Git workflow may fail in standard shell usage | Use absolute paths during validation; fix PATH before relying on normal commands |
| Validation currently uses Java 25 runtime instead of Java 21 runtime | Runtime-specific warnings or behavior may differ from target Java version | Install/configure Java 21 and run full validation with Java 21 |
| Repository starts empty | More bootstrap work is required | Build incrementally and validate after each milestone |
| H2 is not production database technology | Real production deployment would need stronger persistence | Document as assignment constraint and isolate persistence through repository layer |
| Aggregate analytics fields may become write hot spots | High redirect volume could cause contention on a single row | Accept for assignment scope; revisit in performance milestone |
| File-backed H2 creates local database files | Local state can affect manual testing | Database files are ignored by Git; tests use isolated in-memory database behavior |
| URL validation is currently minimal on DTO | Invalid but nonblank URLs may pass DTO validation until validation milestone | Add dedicated URL validation in the validation milestone |
| Mapper is hand-written | Mapping drift can occur as fields grow | Cover mapper behavior with unit tests; reconsider MapStruct if mapping complexity grows |
| Random short-code generation can collide | Create requests could fail if repeated collisions occur | Repository uniqueness check and bounded retry handling |
| Short-code length may need future adjustment | 7 characters may be insufficient for very large scale | Document as initial setting; revisit in performance/readiness milestone |
| HTTPS-only validation may reject legitimate HTTP use cases | Some internal/local URLs cannot be shortened | Accept as secure default; revisit if business requirement requires HTTP |
| Configured base URL may be wrong | API can return unusable short URLs | Keep base URL externalized and document setup requirement |

## Rollback Approach

Milestone 1 changes are limited to project skeleton files. Rollback can be performed by reverting the milestone commit after Git workflow is available.
