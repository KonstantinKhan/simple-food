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

# Run single test class (example for transport-mappers)
./gradlew :transport-mappers:test --tests "com.khan366kos.mapper.ProductMapperTest"
```

### OpenAPI Model Generation
```bash
# Regenerate transport models from OpenAPI specs
./gradlew :transport-models:openApiGenerate

# Clean and regenerate
./gradlew :transport-models:clean :transport-models:openApiGenerate
```

## Module Architecture

The project uses a 4-module architecture:

### 1. `common-models`
- **Purpose**: Business/domain models (Be* prefix)
- **No external dependencies** at runtime
- **Key types**: `BeProduct`, `BeDish`, `BeAuthor`, `BeWeight`, `BeMeasure`, `BeId`
- **Value classes**: `BeCategories`, `BeCategory`, `BeCalories`, `BeProteins`, `BeFats`, `BeCarbohydrates`
- **Repository interfaces**: `IRepoProduct`, database request/response models

### 2. `transport-models`
- **Purpose**: OpenAPI-generated DTOs for REST API
- **Generated from**: `specs/spec-simple-food-api.yaml` (which references products and dishes specs)
- **Package**: `com.khan366kos.transport.model`
- **Auto-regenerated** on build via OpenAPI Generator plugin
- **Do not manually edit** files in `build/generated/openapi/`

### 3. `transport-mappers`
- **Purpose**: Bidirectional conversion between transport and business models
- **Dependencies**: `common-models`, `transport-models`
- **Key functions**:
  - `Product.toContext()` - transport → business
  - `BeProduct.toTransport()` - business → transport
  - Same pattern for Dish and common types (Author, Weight, etc.)
- **Tests**: Kotest-based, ensure bidirectional mapping correctness

### 4. `simple-food-product-app`
- **Purpose**: Ktor REST API application
- **Main class**: `com.khan366kos.Application`
- **Entry point**: `io.ktor.server.netty.EngineMain`
- **Structure**:
  - `Application.kt` - module configuration
  - `HTTP.kt` - CORS, ContentNegotiation (Jackson)
  - `Routing.kt` - route registration
  - `plugins/StatusPages.kt` - global error handling
  - `routes/ProductRoutes.kt` - REST endpoints
  - `repository/ProductRepository.kt` - in-memory ConcurrentHashMap storage
  - `repository/TestData.kt` - seed data

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
- All repositories implement `IRepo*` interfaces from common-models
- Request/Response wrappers: `DbProductRequest`, `DbProductResponse`, etc.
- Thread-safe in-memory storage using `ConcurrentHashMap`
- Returns `DbProductsResponse` with `isSuccess` flag and `result` list/object

## OpenAPI Specifications

Located in `specs/`:
- `spec-simple-food-api.yaml` - Main spec referencing others
- `spec-simple-food-api-products.yaml` - Product endpoints
- `spec-simple-food-api-dishes.yaml` - Dish endpoints
- `spec-simple-food-common.yaml` - Shared schemas (Author, Weight, NutritionalValue)

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

### Testing
- Transport mappers use Kotest: `shouldBe`, `forAll`, property-based tests
- API endpoints tested via curl or `TEST_COMMANDS.sh` script
- Repository tests ensure thread-safety

## Important Notes

- **Java 21** is required (specified in jvmToolchain)
- **Port 8080** is default, override with `PORT` environment variable
- **CORS enabled** for all origins (development configuration)
- **In-memory storage** - data is lost on restart
- **Jackson serialization** - not kotlinx.serialization
- Test data includes 3 products: Chicken breast, White rice, Olive oil (see `TestData.kt`)
