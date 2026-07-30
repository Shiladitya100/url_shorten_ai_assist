# Architecture

## Style

The application will use Clean Architecture with the following dependency direction:

```text
controller
  -> service
  -> repository
  -> database
```

Controllers expose HTTP APIs. Services own application use cases. Repositories isolate persistence. Entities represent database state.

## Initial Package Structure

```text
com.schwab.urlshortener
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── service
├── service.impl
├── util
└── validation
```

## Current Milestone

Milestone 1 establishes the project skeleton only. No domain model or endpoint behavior has been implemented yet.
