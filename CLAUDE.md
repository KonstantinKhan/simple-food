# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Simple Food is a Kotlin/Ktor multi-module food product and dish management API. The project uses OpenAPI specifications to generate transport models and follows a clean architecture with separation between transport, business, and data layers.

## Common Commands

### Build and Run
```bash
# Build entire project
./gradlew build

# Build specific module
./gradlew :simple-food-product-app:build
./gradlew :transport-models:build

# Run the application (starts on port 8080)
./gradlew :simple-food-product-app:run

# Build fat JAR
./gradlew buildFatJar
```

### Testing
```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :transport-mappers:test
./gradlew :measures:test

# Run single test class
./gradlew :transport-mappers:test --tests "com.khan366kos.mapper.ProductMapperTest"
./gradlew :measures:test --tests "com.khan366kos.measures.repository.MeasureRepositoryTest"
```

### OpenAPI Model Generation
```bash
# Regenerate transport models from OpenAPI specs
./gradlew :transport-models:openApiGenerate

# Clean and regenerate
./gradlew :transport-models:clean :transport-models:openApiGenerate
```

## Module Architecture

The project uses a 7-module architecture:

### 1. `simple-food-common-models`
- **Purpose**: Business/domain models (Be* prefix) shared across products and dishes
- **No external dependencies** at runtime
- **Key types**: `BeProduct`, `BeDish`, `BeAuthor`, `BeWeight`, `BeId`
- **Value classes**: `BeCategories`, `BeCategory`, `BeCalories`, `BeProteins`, `BeFats`, `BeCarbohydrates`
- **Repository interfaces**: `IRepoProduct`, database request/response models
- **Note**: `BeMeasure` has been moved to the `measures` module

### 2. `measures`
- **Purpose**: Standalone module for units of measurement (gram, kilogram, liter, etc.)
- **Dependencies**: `simple-food-common-models` (for `BeId`)
- **Key types**: `BeMeasure`, `BeMeasureTranslation`, `BeMeasureWithTranslations`
- **Repository interfaces**: `IRepoMeasure` with request/response models
- **Implementations**:
  - `MeasureRepositoryInMemory` - Thread-safe in-memory storage
  - `MeasureRepositoryPostgres` - Persistent PostgreSQL storage with Exposed ORM
- **Database tables**: `measures`, `measure_translations` (i18n support)
- **Use case**: Centralized measure management with multilingual support (ru/en)

### 3. `transport-models`
- **Purpose**: OpenAPI-generated DTOs for REST API
- **Generated from**: `specs/spec-simple-food-api.yaml` (which references products and dishes specs)
- **Package**: `com.khan366kos.transport.model`
- **Auto-regenerated** on build via OpenAPI Generator plugin
- **Do not manually edit** files in `build/generated/openapi/`

### 4. `transport-mappers`
- **Purpose**: Bidirectional conversion between transport and business models
- **Dependencies**: `simple-food-common-models`, `transport-models`
- **Key functions**:
  - `Product.toContext()` - transport → business
  - `BeProduct.toTransport()` - business → transport
  - Same pattern for Dish and common types (Author, Weight, etc.)
- **Tests**: Kotest-based, ensure bidirectional mapping correctness

### 5. `repo-in-memory`
- **Purpose**: In-memory repository implementation using `ConcurrentHashMap`
- **Dependencies**: `simple-food-common-models`
- **Implementation**: `ProductRepository` class
- **Storage**: Thread-safe in-memory storage (data lost on restart)
- **Test data**: Pre-loads 3 sample products on initialization
- **Use case**: Development, testing, and quick prototyping

### 6. `repo-postgresql`
- **Purpose**: PostgreSQL repository implementation using Exposed ORM (DSL API)
- **Dependencies**: `simple-food-common-models`, Exposed, PostgreSQL driver, Flyway, HikariCP
- **Implementation**: `ProductRepositoryPostgres` class
- **Storage**: Persistent PostgreSQL database with connection pooling
- **Migrations**: Flyway-managed schema migrations in `src/main/resources/db/migration/`
- **Tables**: `products`, `product_categories` (junction table), `measures`, `measure_translations`
- **Use case**: Production deployments requiring persistent storage

### 7. `simple-food-product-app`
- **Purpose**: Ktor REST API application
- **Main class**: `com.khan366kos.Application`
- **Entry point**: `io.ktor.server.netty.EngineMain`
- **Structure**:
  - `Application.kt` - module configuration and database initialization
  - `Database.kt` - database connection setup based on repository type
  - `HTTP.kt` - CORS, ContentNegotiation (Jackson)
  - `Routing.kt` - route registration and repository selection
  - `plugins/StatusPages.kt` - global error handling
  - `routes/ProductRoutes.kt` - REST endpoints for products
  - `routes/MeasureRoutes.kt` - REST endpoints for measures
- **Repository Selection**: Configured via `application.conf` (defaults to in-memory)

## Key Architectural Patterns

### Model Transformation Flow
```
HTTP Request (JSON)
  ↓ (Jackson deserialization)
Transport Model (Product, Dish)
  ↓ (.toContext())
Business Model (BeProduct, BeDish)
  ↓ (Repository operations)
Business Model (BeProduct, BeDish)
  ↓ (.toTransport())
Transport Model (Product, Dish)
  ↓ (Jackson serialization)
HTTP Response (JSON)
```

### Error Handling
- Global exception handling via `StatusPages` plugin
- Standard error format from OpenAPI spec:
  ```kotlin
  Error(code = "ERROR_CODE", message = "Description", details = mapOf(...))
  ```
- Error codes: `BAD_REQUEST`, `NOT_FOUND`, `INTERNAL_SERVER_ERROR`

### Repository Pattern
- All repositories implement `IRepo*` interfaces from simple-food-common-models
- Request/Response wrappers: `DbProductRequest`, `DbProductResponse`, etc.
- Returns `DbProductsResponse` with `isSuccess` flag and `result` list/object
- **Two implementations available**:
  - `repo-in-memory`: Thread-safe ConcurrentHashMap (default)
  - `repo-postgresql`: Persistent PostgreSQL with Exposed ORM
- Repository selection via `repository.type` config (`memory` or `postgres`)

## OpenAPI Specifications

Located in `specs/`:
- `spec-simple-food-api.yaml` - Main spec referencing others
- `spec-simple-food-api-products.yaml` - Product endpoints
- `spec-simple-food-api-dishes.yaml` - Dish endpoints
- `spec-simple-food-api-measures.yaml` - Measure endpoints (CRUD for units of measurement)
- `spec-simple-food-common.yaml` - Shared schemas (Author, Weight, NutritionalValue, Measure)

When modifying specs:
1. Update the appropriate YAML file
2. Run `./gradlew :transport-models:openApiGenerate`
3. Update mappers in `transport-mappers` if schema changed
4. Update route handlers if endpoints changed

## Development Workflow

### Adding New Endpoints
1. Update OpenAPI spec in `specs/`
2. Regenerate transport models: `./gradlew :transport-models:openApiGenerate`
3. Add/update mappers in `transport-mappers` with tests
4. Implement route handler in `simple-food-product-app/routes/`
5. Use mappers: `transportModel.toContext()` and `businessModel.toTransport()`

### Working with Models
- **Always use mappers** - never manually construct conversions
- Business models use value classes for type safety (BeId wraps UUID)
- Transport models are Jackson-annotated POJOs
- UUID handling: `BeId(UUID.fromString(idParam))` for path parameters

### Working with Measures
- Measures are stored in a separate `measures` module with their own repository
- Each measure has a unique `code` (e.g., "GRAM", "KILOGRAM") and supports i18n
- Measure translations provide localized names (ru/en) via `measure_translations` table
- Products reference measures via foreign keys (calories_measure_id, proteins_measure_id, etc.)
- Pre-seeded measures: GRAM, KILOGRAM, LITER, MILLILITER, PIECE, SERVING, KILOCALORIE
- Both in-memory and PostgreSQL implementations available

### Testing
- Transport mappers use Kotest: `shouldBe`, `forAll`, property-based tests
- API endpoints tested via curl or `TEST_COMMANDS.sh` script
- Repository tests ensure thread-safety
- Measure repositories include tests for CRUD operations and translations

## PostgreSQL Database Setup

### Configuration

The application supports two repository modes configured in `application.conf`:

```hocon
repository {
    type = "memory"  # Options: "memory" (default) or "postgres"
}

postgres {
    jdbcUrl = "jdbc:postgresql://localhost:5432/simplefood"
    driver = "org.postgresql.Driver"
    user = "postgres"
    password = "postgres"
    maxPoolSize = 10
}
```

**Environment Variable Overrides:**
- `REPOSITORY_TYPE` - Override repository type
- `DB_URL` - Override JDBC URL
- `DB_USER` - Override database user
- `DB_PASSWORD` - Override database password
- `DB_MAX_POOL_SIZE` - Override connection pool size

### Local Development with Docker

Quick start with Docker Compose:

```bash
# Create docker-compose.yml
cat > docker-compose.yml << 'EOF'
version: '3.8'
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: simplefood
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
EOF

# Start PostgreSQL
docker-compose up -d

# Run application with PostgreSQL
REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run
```

### Database Migrations

Migrations are automatically run on application startup via Flyway:

- **V1__create_products_table.sql** - Creates products schema (products, product_categories tables)
- **V2__insert_test_data.sql** - Seeds database with 3 sample products
- **V3__create_measures_tables.sql** - Creates measures and measure_translations tables
- **V4__migrate_measures_to_normalized_tables.sql** - Migrates measures to normalized structure, adds measure_id foreign keys to products table, seeds common measures (GRAM, KILOGRAM, LITER, MILLILITER, PIECE, SERVING, KILOCALORIE) with ru/en translations

**Migration location:** `repo-postgresql/src/main/resources/db/migration/`

**Schema details:**
- `products` table: 40+ columns (flattened BeProduct structure with nutritional data) with foreign keys to measures
- `product_categories` table: Junction table for product-category relationships
- `measures` table: Stores measurement units (id, code, created_at)
- `measure_translations` table: i18n support for measure names (locale, measure_name, measure_short_name)
- Indexes on `name`, foreign keys, and `code` for performance

### Switching Repository Implementations

**In-memory (default):**
```bash
./gradlew :simple-food-product-app:run
```

**PostgreSQL:**
```bash
REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run
```

Or update `application.conf`:
```hocon
repository {
    type = "postgres"
}
```

### Database Access

Connect to PostgreSQL:
```bash
# Using psql
psql -h localhost -U postgres -d simplefood

# View products
SELECT id, name FROM products;

# View categories
SELECT p.name, pc.category
FROM products p
JOIN product_categories pc ON p.id = pc.product_id;

# View measures with translations
SELECT m.code, mt.locale, mt.measure_name, mt.measure_short_name
FROM measures m
JOIN measure_translations mt ON m.id = mt.measure_id
ORDER BY m.code, mt.locale;

# View product with measure information
SELECT p.name, p.calories_value, cm.code as calories_measure
FROM products p
JOIN measures cm ON p.calories_measure_id = cm.id
LIMIT 5;
```

## Important Notes

- **Java 21** is required (specified in jvmToolchain)
- **Port 8080** is default, override with `PORT` environment variable
- **CORS enabled** for all origins (development configuration)
- **Storage options**:
  - In-memory (default): Data is lost on restart, fast development/testing
  - PostgreSQL: Persistent storage with Flyway migrations
- **Jackson serialization** - not kotlinx.serialization
- **Test data**:
  - 3 sample products: Chicken breast, White rice, Olive oil (loaded in both modes)
  - 7 pre-seeded measures with ru/en translations: GRAM, KILOGRAM, LITER, MILLILITER, PIECE, SERVING, KILOCALORIE
- **Internationalization**: Measures support multilingual names (Russian and English)
