# CLAUDE.md

Guide for Claude AI to work with Simple Food project - Kotlin/Ktor API for food product management.

## Quick Commands

```bash
# Build
./gradlew build

# Run (in-memory)
./gradlew :simple-food-product-app:run

# Run (PostgreSQL)
REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run

# Tests
./gradlew test

# Generate OpenAPI models
./gradlew :simple-food-transport-models:openApiGenerate
```

## Project Structure

**7 modules:**

| Module | Description |
|--------|-------------|
| `simple-food-common-models` | Business models (Be* prefix) |
| `simple-food-measures` | Units of measurement with i18n (ru/en) |
| `simple-food-transport-models` | OpenAPI-generated DTOs |
| `simple-food-transport-mappers` | Business ↔ Transport mapping |
| `simple-food-repo-in-memory` | ConcurrentHashMap repository |
| `simple-food-repo-postgresql` | PostgreSQL + Exposed + Flyway |
| `simple-food-product-app` | Ktor REST API (port 8080) |

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

- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) - architecture details
- [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) - developer guide
- [docs/API_REFERENCE.md](docs/API_REFERENCE.md) - API endpoints reference

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

**Version:** 1.1  
**Updated:** November 10, 2025
