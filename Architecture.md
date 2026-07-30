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

Milestone 7 formalizes expiration support across create and redirect flows.

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

## Domain Behavior

`UrlMapping` owns basic domain behavior that is independent of HTTP and service orchestration:

- Determine whether a mapping is expired at a reference time.
- Determine whether a mapping is redirectable.
- Record access count and last access timestamp.

Keeping this behavior on the entity prevents duplicated expiration and analytics mutation logic in future services.

## DTO and Mapper Boundary

DTOs are immutable Java records. The mapper converts persistence entities to API-facing response models. The mapper is hand-written for now because the mapping surface is small and does not justify adding another code-generation dependency.

## Short-Code Generation

Short-code generation is split into two responsibilities:

- `ShortCodeGenerator` creates random candidate codes.
- `ShortCodeGenerationService` checks candidate uniqueness against persisted URL mappings.

The default generator uses a Base62 alphabet and `SecureRandom`. The uniqueness service uses bounded retries to avoid infinite loops if collisions repeatedly occur.

## Create URL Flow

```text
UrlController
  -> UrlShorteningService
  -> ShortCodeGenerationService
  -> UrlMappingRepository
  -> UrlMappingMapper
```

The controller owns HTTP request/response concerns. The service owns use-case orchestration: generate code, create entity, persist mapping, and map response.

## Redirect Flow

```text
RedirectController
  -> UrlShorteningService
  -> UrlMappingRepository
  -> UrlMapping
```

The redirect controller owns HTTP status translation. The service owns lookup, expiration/active-state checks, and access-count mutation. Service exceptions remain framework-free so later global exception handling can centralize HTTP mapping without coupling the service layer to Spring MVC.

## Expiration Behavior

Expiration is represented by the optional `expires_at` column. A URL without `expires_at` does not expire. A URL with `expires_at` is expired when the reference time is equal to or later than the stored timestamp.

Create requests reject past expiration timestamps at the API boundary. Redirect requests return `410 Gone` for expired mappings and do not update analytics counters.

## Analytics Flow

```text
UrlController
  -> UrlShorteningService
  -> UrlMappingRepository
  -> UrlMappingMapper
```

Analytics are stored as aggregate fields on `url_mappings`: `access_count` and `last_accessed_at`. The analytics endpoint returns current aggregate state and computes the `expired` response flag at request time.
