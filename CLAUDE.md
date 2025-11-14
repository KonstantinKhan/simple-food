# CLAUDE.md

Guide for Claude AI to work with Simple Food project - Kotlin/Ktor server app for food management.

## Project Structure

The project implements a multi-module architecture ([docs/achitecture.md](docs/architecture.md))

### Modules

- **simple-food-transport-models** - Transport
  models ([docs/modules/simple-food-transpsort-models](docs/modules/simple-food-transport-models.md))
- **simple-food-common-models** - Common domain models and repository interfaces and
  DTOs ([docs/modules/simple-food-common-models.md](docs/modules/simple-food-common-models.md))

| Module                                                                     | Description                                   |
|----------------------------------------------------------------------------|-----------------------------------------------|
| [simple-food-transport-mappers](simple-food-transport-mappers)             | Business ↔ Transport mapping                  |
| [simple-food-product-repo-memory](simple-food-product-repo-memory)         | In-memory product repository                  |
| [simple-food-product-repo-postgresql](simple-food-product-repo-postgresql) | PostgreSQL product repository                 |
| [simple-food-repo-measure-memory](simple-food-repo-measure-memory)         | In-memory measure repository (7 units, ru/en) |
| [simple-food-repo-measure-postgres](simple-food-repo-measure-postgres)     | PostgreSQL measure repository                 |
| [simple-food-product-app](simple-food-product-app)                         | Ktor REST API (port 8080)                     |
| [specs](specs)                                                             | OpenAPI specification                         |

## Quick Commands

### Build

`./gradlew build`

### Run (in-memory)

`./gradlew :simple-food-product-app:run`

### Run (PostgreSQL)

`REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run`

### Generate OpenAPI models

`./gradlew :simple-food-transport-models:openApiGenerate`

## Data Flow

```text
HTTP → Transport DTO → Business Model → Repository
       (toContext())                   (IRepoProduct)
                                            ↓
Repository → Business Model → Transport DTO → HTTP
             (toTransport())
```

## Key Patterns

### Working with Models

```kotlin
// Transport → Business
val beProduct = transportProduct.toContext()

// Business → Transport
val transportProduct = beProduct.toTransport()
```

### Repositories

**Interfaces:** `IRepoProduct`, `IRepoMeasure` in `simple-food-common-models`

**Implementations:**

- `memory` (default) - ConcurrentHashMap, volatile
- `postgres` - PostgreSQL, Exposed ORM, Flyway migrations

**Configuration:**

```hocon
repository.type = "memory"  # or "postgres"
```

Environment variable: `REPOSITORY_TYPE`

### OpenAPI Workflow

1. Update spec in `specs/spec-simple-food-api-*.yaml`
2. Regenerate: `./gradlew :simple-food-transport-models:openApiGenerate`
3. Update mapping in `simple-food-transport-mappers` + tests
4. Implement routes using `.toContext()` and `.toTransport()`

## Database

### Quick Start PostgreSQL (Docker)

```bash
docker run -d -p 5432:5432 \
  -e POSTGRES_DB=simplefood \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  postgres:16
```

### Environment Variables

```bash
REPOSITORY_TYPE=postgres
DB_URL=jdbc:postgresql://localhost:5432/simplefood
DB_USER=postgres
DB_PASSWORD=postgres
```

## Important Details

- **Java:** 21+
- **Port:** 8080 (override: `PORT` env var)
- **Test data:** 3 products + 7 units of measurement (ru/en)
- **Error format:** OpenAPI `Error(code, message, details)`
- **CORS:** configured for development
- **Mapping:** always use mappers, never manually

## Documentation

- [docs/achitecture.md](docs/architecture.md) - architecture details
- [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) - developer guide
- [docs/API_REFERENCE.md](docs/API_REFERENCE.md) - API endpoints reference
- [docs/TESTING.md](docs/TESTING.md)

## Technologies

- Kotlin 2.2.20
- Ktor 3.3.1
- Jackson (JSON)
- OpenAPI Generator
- PostgreSQL 16 + Exposed ORM
- Flyway (migrations)
- Gradle 8.5+

## Configuration

`simple-food-product-app/src/main/resources/application.conf`:

```hocon
ktor {
  deployment {
    port = 8080
    port = ${?PORT}
  }
}

repository {
  type = "memory"
  type = ${?REPOSITORY_TYPE}
}

postgres {
  jdbcUrl = "jdbc:postgresql://localhost:5432/simplefood"
  jdbcUrl = ${?DB_URL}
  username = ${?DB_USER}
  password = ${?DB_PASSWORD}
}
```

---

**Version:** 1.2
**Updated:** November 12, 2025
