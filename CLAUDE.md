# CLAUDE.md

Project guidance for Kotlin/Ktor multi-module food management API with OpenAPI specs and clean architecture.

## Quick Commands

### build

`./gradlew build`

### run

`./gradlew :simple-food-product-app:run`

## Testing

### all tests

`./gradlew test`

### concrete test

`./gradlew :transport-mappers:test --tests "com.khan366kos.mapper.ProductMapperTest"`

## OpenAPI Model Generation

`./gradlew :transport-models:openApiGenerate`

Architecture Summary
7 Modules:

- simple-food-common-models - Business models (Be* prefix)
- measures - Units of measurement with i18n
- transport-models - OpenAPI-generated DTOs
- transport-mappers - Business ↔ Transport model conversion
- simple-food-repo-in-memory - ConcurrentHashMap storage
- repo-postgresql - PostgreSQL with Exposed ORM
- simple-food-product-app - Ktor REST API

## Key Patterns

### Model Flow

```text
HTTP Request → Transport Model → Business Model → Repository → Response
    (Jackson)      (toContext())                 (toTransport())
```

## Repository Pattern

- Interfaces: IRepoProduct, IRepoMeasure in common-models
- Implementations:
    - memory (default) - ConcurrentHashMap, volatile
    - postgres - PostgreSQL with Flyway migrations
- Config: repository.type in application.conf or REPOSITORY_TYPE env

## Development Guide

### Adding Features

1. Update OpenAPI spec in specs/
2. Regenerate: ./gradlew :transport-models:openApiGenerate
3. Update mappers in transport-mappers with tests
4. Implement routes using .toContext() and .toTransport()

## Database Setup

```bash
# Quick PostgreSQL with Docker
docker run -d -p 5432:5432 -e POSTGRES_DB=simplefood -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres:16

# Run with PostgreSQL
REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run
```

## Important Notes

- Java 21 required
- Port 8080 default, override with PORT env
- Test data: 3 sample products + 7 measures with ru/en translations
- Error format: Standardized Error(code, message, details) from OpenAPI
- Always use mappers - never manual model conversions

## Configuration

```hocon
# application.conf
repository.type = "memory"  # or "postgres"
postgres.jdbcUrl = "jdbc:postgresql://localhost:5432/simplefood"
```

Environment variables: REPOSITORY_TYPE, DB_URL, DB_USER, DB_PASSWORD
