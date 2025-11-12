# Simple Food Project Architecture

## Module Structure

| Module                                                         | Description                            |
| -------------------------------------------------------------- | -------------------------------------- |
| [simple-food-common-models](simple-food-common-models)         | Business models (Be\* prefix)          |
| [simple-food-measures](simple-food-measures)                   | Units of measurement with i18n (ru/en) |
| [simple-food-transport-models](simple-food-transport-models)   | OpenAPI-generated DTOs                 |
| [simple-food-transport-mappers](simple-food-transport-mappers) | Business ↔ Transport mapping           |
| [simple-food-repo-in-memory](simple-food-repo-in-memory)       | ConcurrentHashMap repository           |
| [simple-food-repo-postgresql](simple-food-repo-postgresql)     | PostgreSQL + Exposed + Flyway          |
| [simple-food-product-app](simple-food-product-app)             | Ktor REST API (port 8080)              |
| [specs](specs)                                                 | OpenAPI specification                  |

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
**Business models with Be* prefix**

- `BeProduct` - food product
- `BeMeasure` - unit of measurement
- `BeAuthor` - record author
- Value classes: `BeCategories`, `BeCalories`, `BeProteins`, `BeFats`, `BeCarbohydrates`
- Repository interfaces: `IRepoProduct`, `IRepoMeasure`

### 2. simple-food-measures
**Unit of measurement system**

- 7 predefined units: gram, kilogram, liter, milliliter, piece, tablespoon, teaspoon
- i18n support (ru/en)
- Initialization via `MeasureCache.initMeasures()`

### 3. simple-food-transport-models
**OpenAPI-generated models**

Generated from specifications:
- `specs/spec-simple-food-api-products.yaml`
- `specs/spec-simple-food-api-measures.yaml`
- `specs/spec-simple-food-api-dishes.yaml`
- `specs/spec-simple-food-common.yaml`

### 4. simple-food-transport-mappers
**Model transformation**

- `Product.toContext()` → `BeProduct`
- `BeProduct.toTransport()` → `Product`
- Tests: `ProductMapperTest`, `MeasureMapperTest`

### 5. simple-food-repo-in-memory
**In-memory storage**

- `RepoProductInMemory` - ConcurrentHashMap
- Thread-safe operations
- Volatile for updates
- 3 test products on initialization

### 6. simple-food-repo-postgresql
**PostgreSQL storage**

- Exposed ORM
- Flyway migrations: `db/migration/V1__Init.sql`
- Tables: `products`, `measures`
- Test data initialization: `V2__Insert_measures.sql`, `V3__Insert_test_products.sql`

### 7. simple-food-product-app
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
- **memory** (default) - `RepoProductInMemory`
- **postgres** - `RepoProductPostgres`

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
