# Developer Guide

## Requirements

- **Java:** 21+
- **Gradle:** 8.5+
- **PostgreSQL:** 16+ (optional)

## Quick Start

### Build Project
```bash
./gradlew build
```

### Run Application
```bash
# With in-memory repository (default)
./gradlew :simple-food-product-app:run

# With PostgreSQL
REPOSITORY_TYPE=postgres ./gradlew :simple-food-product-app:run
```

Application available at `http://localhost:8080`

### Testing
```bash
# All tests
./gradlew test

# Specific test
./gradlew :simple-food-transport-mappers:test --tests "com.khan366kos.mapper.ProductMapperTest"

# Module tests
./gradlew :simple-food-repo-in-memory:test
```

## Working with OpenAPI

### Generate Models
```bash
./gradlew :simple-food-transport-models:openApiGenerate
```

Models are generated in:
```
simple-food-transport-models/build/generated/openapi/
└── src/main/kotlin/
    └── com/khan366kos/api/v1/models/
```

### Update API

1. Edit specification in `specs/`
2. Regenerate models:
   ```bash
   ./gradlew :simple-food-transport-models:openApiGenerate
   ```
3. Update mapping in `simple-food-transport-mappers`
4. Add tests for new mappings
5. Implement routes in `simple-food-product-app`

## Adding New Feature

### Example: Adding Product Categories

1. **OpenAPI specification** (`specs/spec-simple-food-api-products.yaml`):
```yaml
components:
  schemas:
    ProductCategory:
      type: object
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string
```

2. **Regenerate models**:
```bash
./gradlew :simple-food-transport-models:openApiGenerate
```

3. **Business model** (`simple-food-common-models`):
```kotlin
data class BeProductCategory(
    val id: BeId = BeId.NONE,
    val name: String = ""
)
```

4. **Mapping** (`simple-food-transport-mappers`):
```kotlin
fun ProductCategory.toContext() = BeProductCategory(
    id = BeId(id ?: ""),
    name = name ?: ""
)

fun BeProductCategory.toTransport() = ProductCategory(
    id = id.asString(),
    name = name
)
```

5. **Mapping test**:
```kotlin
class ProductCategoryMapperTest {
    @Test
    fun `should map ProductCategory to BeProductCategory`() {
        val category = ProductCategory(
            id = "test-id",
            name = "Dairy Products"
        )
        val result = category.toContext()
        assertEquals("test-id", result.id.asString())
        assertEquals("Dairy Products", result.name)
    }
}
```

6. **Endpoint** (`simple-food-product-app/Routing.kt`):
```kotlin
route("/categories") {
    get {
        val categories = categoryRepository.list()
        call.respond(categories.map { it.toTransport() })
    }
}
```

## Database Setup

### Docker (quick start)
```bash
docker run -d \
  --name simplefood-db \
  -p 5432:5432 \
  -e POSTGRES_DB=simplefood \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  postgres:16
```

### Migrations

Flyway migrations are located in:
```
simple-food-repo-postgresql/src/main/resources/db/migration/
├── V1__Init.sql                    # Create tables
├── V2__Insert_measures.sql         # Units of measurement
└── V3__Insert_test_products.sql    # Test data
```

Executed automatically on application startup.

### Adding New Migration

1. Create file `V4__Description.sql`
2. Write SQL:
```sql
ALTER TABLE products ADD COLUMN is_verified BOOLEAN DEFAULT false;
```
3. Restart application

## Configuration

### application.conf
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
    username = "postgres"
    username = ${?DB_USER}
    password = "postgres"
    password = ${?DB_PASSWORD}
}
```

### Environment Variables
```bash
# Application port
export PORT=8080

# Repository type
export REPOSITORY_TYPE=postgres

# Database
export DB_URL=jdbc:postgresql://localhost:5432/simplefood
export DB_USER=postgres
export DB_PASSWORD=postgres
```

## Logging

Configuration: `simple-food-product-app/src/main/resources/logback.xml`

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="info">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

## Test Data

### Products
3 test products are created on initialization:
1. Chicken Breast (id: 550e8400-e29b-41d4-a716-446655440001)
2. White Rice (id: 550e8400-e29b-41d4-a716-446655440002)
3. Olive Oil (id: 550e8400-e29b-41d4-a716-446655440003)

### Units of Measurement
7 units with ru/en translation:
- Gram (грамм)
- Kilogram (килограмм)
- Liter (литр)
- Milliliter (миллилитр)
- Piece (штука)
- Tablespoon (столовая ложка)
- Teaspoon (чайная ложка)

## Debugging

### IntelliJ IDEA
1. Create Run Configuration: `Gradle → :simple-food-product-app:run`
2. Set breakpoints
3. Debug → 'simple-food-product-app:run'

### VS Code
See [VSCODE_SETUP.md](../VSCODE_SETUP.md)

## Useful Commands

```bash
# Clean build
./gradlew clean

# Full rebuild
./gradlew clean build

# Run with debug logs
./gradlew :simple-food-product-app:run --debug

# Fat JAR
./gradlew :simple-food-product-app:shadowJar
# Run: java -jar simple-food-product-app/build/libs/simple-food-product-app-all.jar
```
