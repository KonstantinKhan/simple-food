# Simple Food Project Architecture

## Module Structure

| Module                                                                 | Description                               |
|------------------------------------------------------------------------|-------------------------------------------|
| [simple-food-common-models](simple-food-common-models)                 | Business models and repository interfaces |
| [simple-food-transport-models](simple-food-transport-models)           | OpenAPI-generated DTOs                    |
| [simple-food-transport-mappers](simple-food-transport-mappers)         | Business ↔ Transport mapping              |
| [simple-food-repo-in-memory](simple-food-repo-in-memory)               | In-memory product repository              |
| [simple-food-repo-postgresql](simple-food-repo-postgresql)             | PostgreSQL product repository             |
| [simple-food-repo-measure-memory](simple-food-repo-measure-memory)     | In-memory measure repository              |
| [simple-food-repo-measure-postgres](simple-food-repo-measure-postgres) | PostgreSQL measure repository             |
| [simple-food-product-app](simple-food-product-app)                     | Ktor REST API (port 8080)                 |
| [specs](specs)                                                         | OpenAPI specification                     |

## Data Flow

```text
HTTP Request → Transport Model → Business Model → Repository
    (Jackson)     (toContext())                   (IRepoProduct)
                                                        ↓
Repository → Business Model → Transport Model → HTTP Response
             (toTransport())      (Jackson)
```

## Modules

### 1. simple-food-common-models
**Business models with Be* prefix and repository interfaces**

- `BeProduct` - food product
- `BeMeasure` - unit of measurement
- `BeMeasureTranslation` - localized measure translation
- `BeMeasureWithTranslations` - measure with all translations
- `BeAuthor` - record author
- Value classes: `BeCategories`, `BeCalories`, `BeProteins`, `BeFats`, `BeCarbohydrates`
- Repository interfaces: `IRepoProduct`, `IRepoMeasure`
- Repository DTOs: `DbMeasureRequest`, `DbMeasureResponse`, `DbMeasureFilterRequest`, etc.

### 2. simple-food-transport-models
**OpenAPI-generated models**

Generated from specifications:
- `specs/spec-simple-food-api-products.yaml`
- `specs/spec-simple-food-api-measures.yaml`
- `specs/spec-simple-food-api-dishes.yaml`
- `specs/spec-simple-food-common.yaml`

### 3. simple-food-transport-mappers
**Model transformation**

- `Product.toContext()` → `BeProduct`
- `BeProduct.toTransport()` → `Product`
- Tests: `ProductMapperTest`, `MeasureMapperTest`

### 4. simple-food-repo-in-memory
**In-memory storage**

- `RepoProductInMemory` - ConcurrentHashMap
- Thread-safe operations
- Volatile for updates
- 3 test products on initialization

### 5. simple-food-repo-postgresql
**PostgreSQL storage**

- Exposed ORM
- Flyway migrations: `db/migration/V1__Init.sql`
- Tables: `products`, `measures`
- Test data initialization: `V2__Insert_measures.sql`, `V3__Insert_test_products.sql`

### 6. simple-food-repo-measure-memory
**In-memory measure repository**

- `MeasureRepositoryInMemory` - ConcurrentHashMap-based storage
- Thread-safe operations with concurrent maps
- Implements `IRepoMeasure` interface
- Seeded with 7 predefined units on initialization (in-memory mode only)
- Supports filtering by locale and search text

### 7. simple-food-repo-measure-postgres
**PostgreSQL measure repository**

- `MeasureRepositoryPostgres` - Exposed ORM implementation
- Database tables: `measures`, `measure_translations`
- Implements `IRepoMeasure` interface
- 7 predefined units initialized via Flyway migration
- i18n support (ru/en) with extensible translation system
- Database-generated UUIDs and timestamps for new measures

### 8. simple-food-product-app
**Ktor REST API**

Structure:
```
src/main/kotlin/
├── Application.kt        # Entry point
├── HTTP.kt              # Jackson, CORS
├── Routing.kt           # Routing
├── Serialization.kt     # Jackson configuration
└── plugins/
    └── StatusPages.kt   # Error handling
```

## Repository Pattern

### Interfaces
- `IRepoProduct` - CRUD operations for products
- `IRepoMeasure` - Work with units of measurement

### Implementations

**Product Repositories:**
- **memory** (default) - `ProductRepository` (in-memory)
- **postgres** - `ProductRepositoryPostgres`

**Measure Repositories:**
- **memory** (default) - `MeasureRepositoryInMemory` (with seeding)
- **postgres** - `MeasureRepositoryPostgres` (uses Flyway migrations)

### Configuration
```hocon
# application.conf
repository.type = "memory"  # or "postgres"
postgres {
    jdbcUrl = "jdbc:postgresql://localhost:5432/simplefood"
    username = "postgres"
    password = "postgres"
}
```

Environment variables:
- `REPOSITORY_TYPE` - repository type
- `DB_URL`, `DB_USER`, `DB_PASSWORD` - database parameters

## Error Handling

Standard format (from OpenAPI):
```json
{
  "code": "NOT_FOUND",
  "message": "Product with id ... not found",
  "details": []
}
```

Error codes:
- `BAD_REQUEST` (400)
- `NOT_FOUND` (404)
- `INTERNAL_SERVER_ERROR` (500)
