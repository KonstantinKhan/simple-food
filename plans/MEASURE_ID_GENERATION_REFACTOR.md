# Refactoring Plan: Database-Level ID Generation for Measures

## Goal
Make measure creation consistent with products module - client sends no ID, database generates UUID automatically.

## Changes Required

### 1. **OpenAPI Specification** (`specs/spec-simple-food-api-measures.yaml`)
   - Make `id` field **read-only** in POST request body for `/measures`
   - Keep `id` required in responses (GET, PUT)
   - Update `MeasureDetail` schema to indicate ID is server-generated

### 2. **Database Schema** (new migration: `V5__add_uuid_generation_to_measures.sql`)
   - Add `DEFAULT gen_random_uuid()` to `measures.id` column
   - Modify `created_at` to rely on database `DEFAULT CURRENT_TIMESTAMP` (already exists)
   - Migration is safe: only adds defaults, doesn't affect existing data

### 3. **Exposed ORM Tables** (`simple-food-measures/src/.../MeasuresTable.kt`)
   - Remove explicit `id` assignment from INSERT statements
   - Let database generate UUID automatically
   - Use `resultedValues` to retrieve generated ID after insert

### 4. **PostgreSQL Repository** (`MeasureRepositoryPostgres.kt`)
   - Remove code that reads `measure.id` from request
   - Remove explicit `it[id] = measureUuid` from INSERT
   - Capture database-generated ID: `val generatedId = insertStatement[MeasuresTable.id]`
   - Use generated ID for translations foreign key

### 5. **In-Memory Repository** (`MeasureRepositoryInMemory.kt`)
   - Generate new UUID: `val generatedId = BeId(UUID.randomUUID())`
   - Ignore `request.measure.id` completely
   - Create measure copy with generated ID before storing
   - Align behavior with PostgreSQL and products module

### 6. **Transport Mapper** (`Transport2ContextMeasure.kt`)
   - Make `id` parameter optional in `toContext()` conversion
   - For POST requests: ignore/don't use transport ID
   - For PUT requests: preserve ID for updates

### 7. **API Routes** (`MeasureRoutes.kt`)
   - POST endpoint: no changes needed (repository handles ID generation)
   - Ensure response returns the generated ID
   - Verify 201 Created response includes full measure with ID

### 8. **Transport Models** (regenerate after OpenAPI changes)
   - Run `./gradlew :simple-food-transport-models:openApiGenerate`
   - Verify `MeasureDetail` constructor handles optional ID correctly
   - Update mapper tests for new behavior

### 9. **Tests & Validation**
   - Update mapper tests to expect server-generated IDs
   - Test POST without ID returns 201 with generated UUID
   - Test duplicate code returns 400
   - Verify in-memory and PostgreSQL modes behave identically
   - Test translations are properly linked to generated measure ID

## Files to Modify

1. `specs/spec-simple-food-api-measures.yaml` - OpenAPI spec
2. `simple-food-repo-postgresql/src/main/resources/db/migration/V5__add_uuid_generation_to_measures.sql` - new migration
3. `simple-food-measures/src/.../repository/MeasuresTable.kt` - Exposed table
4. `simple-food-measures/src/.../repository/MeasureRepositoryPostgres.kt` - PostgreSQL impl
5. `simple-food-measures/src/.../repository/MeasureRepositoryInMemory.kt` - in-memory impl
6. `simple-food-transport-mappers/src/.../Transport2ContextMeasure.kt` - mapper
7. `simple-food-transport-mappers/src/test/.../MeasureMappersKtTest.kt` - mapper tests

## Execution Order

1. Update OpenAPI spec (breaking API change - document it)
2. Regenerate transport models
3. Create database migration
4. Update Exposed tables
5. Refactor PostgreSQL repository
6. Refactor in-memory repository
7. Update mappers
8. Update/fix tests
9. Run full test suite
10. Test manually with both repository types

## Breaking Change Notice

⚠️ **API Breaking Change**: Clients must stop sending `id` in POST `/measures` requests. The server will now generate all IDs.

## Implementation Details by Module

### In-Memory Approach (simpler, no database defaults)
```kotlin
// MeasureRepositoryInMemory.kt
override fun newMeasure(request: DbMeasureRequest): DbMeasureResponse {
    val generatedId = BeId(UUID.randomUUID())
    val measure = request.measure.copy(
        id = generatedId,
        createdAt = Instant.now()
    )

    if (measures.values.any { it.code == measure.code }) {
        return DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
    }

    measures[generatedId] = measure
    val translations = request.translations.map {
        it.copy(id = generatedId)
    }
    // ... store translations
}
```

### PostgreSQL Approach (with database defaults)
```sql
-- V5__add_uuid_generation_to_measures.sql
ALTER TABLE measures
  ALTER COLUMN id SET DEFAULT gen_random_uuid(),
  ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
```

```kotlin
// MeasureRepositoryPostgres.kt - Let database generate ID
override fun newMeasure(request: DbMeasureRequest): DbMeasureResponse {
    return transaction {
        try {
            val measure = request.measure

            // Check code uniqueness
            val existingCode = MeasuresTable.selectAll()
                .where { MeasuresTable.code eq measure.code }.count()
            if (existingCode > 0) {
                return@transaction DbMeasureResponse(
                    result = BeMeasureWithTranslations.NONE,
                    isSuccess = false
                )
            }

            // Insert without explicit ID - database generates it
            val insertedId = MeasuresTable.insert {
                it[code] = measure.code
                // id and createdAt use database defaults
            } get MeasuresTable.id

            // Insert translations with generated ID
            request.translations.forEach { translation ->
                MeasureTranslationsTable.insert {
                    it[measureId] = insertedId
                    it[locale] = translation.locale
                    // ...
                }
            }

            // Fetch and return created measure
            val createdMeasure = getMeasureById(BeId(insertedId.toString()))
            return@transaction DbMeasureResponse(
                result = createdMeasure,
                isSuccess = true
            )
        } catch (e: Exception) {
            return@transaction DbMeasureResponse(
                result = BeMeasureWithTranslations.NONE,
                isSuccess = false
            )
        }
    }
}
```

## Testing Strategy

1. **Unit Tests**: Verify ID generation in both repository implementations
2. **Integration Tests**: POST new measure, verify ID returned, verify it's a valid UUID
3. **Edge Cases**: Duplicate codes, null translations, concurrent creation
4. **API Tests**: Verify 201 status, response includes generated ID
5. **Consistency Tests**: Same behavior across in-memory and PostgreSQL modes

## Rollback Plan

- Revert OpenAPI spec change
- Revert migration (DROP DEFAULT)
- Restore original repository code
- Regenerate transport models
- Run tests to verify rollback