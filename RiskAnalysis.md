# Risk Analysis

## Current Risks

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Java, Maven, and Git are not on PATH | Build and Git workflow may fail in standard shell usage | Use absolute paths during validation; fix PATH before relying on normal commands |
| Validation currently uses Java 25 runtime instead of Java 21 runtime | Runtime-specific warnings or behavior may differ from target Java version | Install/configure Java 21 and run full validation with Java 21 |
| Repository starts empty | More bootstrap work is required | Build incrementally and validate after each milestone |
| H2 is not production database technology | Real production deployment would need stronger persistence | Document as assignment constraint and isolate persistence through repository layer |

## Rollback Approach

Milestone 1 changes are limited to project skeleton files. Rollback can be performed by reverting the milestone commit after Git workflow is available.
