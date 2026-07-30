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
| URL validation currently relies on Bean Validation annotations | More nuanced business rules, such as blocked domains or local-network URLs, are not enforced yet | Keep standard validation for milestone 5; revisit custom validation in the validation and security milestones |
| Mapper is hand-written | Mapping drift can occur as fields grow | Cover mapper behavior with unit tests; reconsider MapStruct if mapping complexity grows |
| Random short-code generation can collide | Create requests could fail if repeated collisions occur | Repository uniqueness check and bounded retry handling |
| Short-code length may need future adjustment | 7 characters may be insufficient for very large scale | Document as initial setting; revisit in performance/readiness milestone |
| HTTPS-only validation may reject legitimate HTTP use cases | Some internal/local URLs cannot be shortened | Accept as secure default; revisit if business requirement requires HTTP |
| Configured base URL may be wrong | API can return unusable short URLs | Keep base URL externalized and document setup requirement |
| Redirect access counting updates the URL mapping row synchronously | High redirect traffic could create write contention or lost updates under concurrency | Accept for milestone 6; revisit optimistic locking or asynchronous analytics in performance and analytics milestones |
| Redirect controller currently maps service exceptions locally | HTTP error mapping is duplicated if more controllers need the same behavior | Centralize in the global exception handling milestone |
| Root-level `GET /{shortCode}` can overlap with future top-level routes | Future route names may conflict with generated short codes | Keep API routes under `/api`; revisit reserved-code validation in validation/security milestones |
| `410 Gone` reveals that an expired short code existed | Attackers could distinguish expired codes from never-created codes | Accept for current business clarity; normalize to `404 Not Found` later if enumeration resistance becomes a requirement |
| Expiration checks use application time | Clock drift can cause early or late expiration behavior across environments | Use injected `Clock` in service tests and document runtime time synchronization as a readiness concern |
| Analytics expose original URLs | Users with a valid short code can retrieve the destination without following the redirect | Accept for assignment scope; add authorization or owner scoping if multi-user support is introduced |
| Analytics are aggregate only | Product stakeholders may expect per-click history or trend data | Document limitation and revisit event-level analytics only if required |
| Short-code validation is tied to current code length | Future configurable lengths could break valid requests if validation is not updated | Keep rules centralized in `ShortCodeRules`; update from one place if length changes |
| Reserved code list may be incomplete | Future top-level routes could still collide with generated short codes | Update reserved list whenever adding new root-level routes |

## Rollback Approach

Milestone changes are intentionally small and independently committed. Rollback can be performed by reverting the specific milestone commit.
