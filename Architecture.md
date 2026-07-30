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
|-- config
|-- controller
|-- dto
|-- entity
|-- exception
|-- mapper
|-- repository
|-- service
|-- service.impl
|-- util
`-- validation
```

## Current Milestone

Milestone 2 establishes the database baseline and persistence layer. No HTTP endpoint behavior has been implemented yet.

## Persistence Model

The initial persistence model uses a single aggregate table:

```text
url_mappings
|-- id
|-- original_url
|-- short_code
|-- created_at
|-- expires_at
|-- access_count
|-- last_accessed_at
`-- active
```

The `short_code` column is unique and indexed because redirect lookup will use this field on every redirect request.
