package com.khan366kos.measures.repository.postgres

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.repository.DbMeasureIdRequest
import com.khan366kos.common.model.measure.repository.DbMeasureRequest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Instant
import java.util.UUID

class MeasureRepositoryPostgresTest : ShouldSpec({
    lateinit var repository: MeasureRepositoryPostgres
    lateinit var container: PostgreSQLContainer<*>

    beforeSpec {
        // Start PostgreSQL testcontainer
        container = PostgreSQLContainer("postgres:16")
            .withDatabaseName("simplefood_test")
            .withUsername("test")
            .withPassword("test")
        container.start()

        // Run Flyway migrations from simple-food-repo-postgresql module
        val flyway = Flyway.configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .locations("classpath:db/migration")
            .baselineOnMigrate(true)
            .load()
        flyway.migrate()

        // Initialize Exposed database connection
        Database.connect(
            url = container.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = container.username,
            password = container.password
        )

        // Create repository instance
        repository = MeasureRepositoryPostgres()
    }

    afterSpec {
        container.stop()
    }

    context("measures() - Get all measures") {
        should("return all measures with translations") {
            val response = repository.measures()
            println("repository: $repository")

            response.isSuccess.shouldBeTrue()
            response.result.shouldHaveSize(7)
        }

        should("return measures with correct structure") {
            val response = repository.measures()

            response.isSuccess.shouldBeTrue()
            val measures = response.result

            measures.forEach { measureWithTranslations ->
                measureWithTranslations.measure.id shouldNotBe BeId.NONE
                measureWithTranslations.measure.code shouldNotBe ""
                measureWithTranslations.measure.createdAt shouldNotBe Instant.EPOCH
                measureWithTranslations.translations.shouldHaveSize(2) // ru and en
            }
        }

        should("return measures with ru and en translations") {
            val response = repository.measures()

            response.isSuccess.shouldBeTrue()
            val measures = response.result

            measures.forEach { measureWithTranslations ->
                val locales = measureWithTranslations.translations.map { it.locale }.toSet()
                locales shouldBe setOf("ru", "en")
            }
        }

        should("return translations with non-empty names") {
            val response = repository.measures()

            response.isSuccess.shouldBeTrue()
            val measures = response.result

            measures.forEach { measureWithTranslations ->
                measureWithTranslations.translations.forEach { translation ->
                    translation.name shouldNotBe ""
                    translation.shortName shouldNotBe ""
                    translation.locale shouldNotBe ""
                }
            }
        }

        should("include specific measures (GRAM, KILOGRAM, LITER, etc.)") {
            val response = repository.measures()

            response.isSuccess.shouldBeTrue()
            val measureCodes = response.result.map { it.measure.code }.toSet()

            measureCodes shouldBe setOf(
                "GRAM",
                "KILOGRAM",
                "LITER",
                "MILLILITER",
                "PIECE",
                "SERVING",
                "KILOCALORIE"
            )
        }
    }

    context("newMeasure() - Create new measure") {
        should("create a new measure with translations successfully") {
            val newMeasureId = BeId(UUID.randomUUID().toString())
            val measure = BeMeasure(
                id = newMeasureId,
                code = "CUSTOM_UNIT_${System.currentTimeMillis()}",
                createdAt = Instant.now()
            )
            val translations = listOf(
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "en",
                    name = "Custom Unit",
                    shortName = "CU"
                ),
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "ru",
                    name = "Пользовательская единица",
                    shortName = "ПЕ"
                )
            )
            val request = DbMeasureRequest(measure, translations)

            val response = repository.newMeasure(request)

            response.isSuccess.shouldBeTrue()
            response.result.measure.code shouldBe measure.code
            response.result.translations.shouldHaveSize(2)
        }

        should("generate UUID and createdAt from database") {
            val measure = BeMeasure(
                id = BeId.NONE,
                code = "DATABASE_GENERATED_${System.currentTimeMillis()}",
                createdAt = Instant.EPOCH
            )
            val translations = listOf(
                BeMeasureTranslation(
                    id = BeId.NONE,
                    locale = "en",
                    name = "Database Generated",
                    shortName = "DG"
                )
            )
            val request = DbMeasureRequest(measure, translations)

            val response = repository.newMeasure(request)

            response.isSuccess.shouldBeTrue()
            // ID should be generated by database
            response.result.measure.id shouldNotBe BeId.NONE
            // Should be valid UUID
            UUID.fromString(response.result.measure.id.value) shouldNotBe null
            // createdAt should be set (not EPOCH)
            response.result.measure.createdAt shouldNotBe Instant.EPOCH
        }

        should("reject duplicate measure code") {
            val existingMeasure = repository.measures().result.first()
            val measure = BeMeasure(
                id = BeId(UUID.randomUUID().toString()),
                code = existingMeasure.measure.code, // Use existing code
                createdAt = Instant.now()
            )
            val translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.randomUUID().toString()),
                    locale = "en",
                    name = "Duplicate",
                    shortName = "DUP"
                )
            )
            val request = DbMeasureRequest(measure, translations)

            val response = repository.newMeasure(request)

            response.isSuccess shouldBe false
        }

        should("allow multiple translations for a new measure") {
            val newMeasureId = BeId(UUID.randomUUID().toString())
            val measure = BeMeasure(
                id = newMeasureId,
                code = "MULTI_LANG_${System.currentTimeMillis()}",
                createdAt = Instant.now()
            )
            val translations = listOf(
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "en",
                    name = "English Name",
                    shortName = "EN"
                ),
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "ru",
                    name = "Русское имя",
                    shortName = "РУ"
                ),
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "fr",
                    name = "Nom Français",
                    shortName = "FR"
                )
            )
            val request = DbMeasureRequest(measure, translations)

            val response = repository.newMeasure(request)

            response.isSuccess.shouldBeTrue()
            response.result.translations.shouldHaveSize(3)
        }

        should("persist new measure and retrieve it") {
            val newMeasureId = BeId(UUID.randomUUID().toString())
            val testCode = "PERSISTENT_${System.currentTimeMillis()}"
            val measure = BeMeasure(
                id = newMeasureId,
                code = testCode,
                createdAt = Instant.now()
            )
            val translations = listOf(
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "en",
                    name = "Persistent Measure",
                    shortName = "PM"
                )
            )
            val request = DbMeasureRequest(measure, translations)

            // Create measure
            val createResponse = repository.newMeasure(request)
            createResponse.isSuccess.shouldBeTrue()

            val createdId = createResponse.result.measure.id

            // Retrieve measure by ID
            val retrievedResponse = repository.measure(DbMeasureIdRequest(createdId))

            retrievedResponse.isSuccess.shouldBeTrue()
            retrievedResponse.result.measure.code shouldBe testCode
            retrievedResponse.result.measure.id shouldBe createdId
        }

        should("persist measure with correct translations") {
            val newMeasureId = BeId(UUID.randomUUID().toString())
            val testCode = "TRANS_CHECK_${System.currentTimeMillis()}"
            val measure = BeMeasure(
                id = newMeasureId,
                code = testCode,
                createdAt = Instant.now()
            )
            val englishName = "Translation Check"
            val englishShort = "TC"
            val russianName = "Проверка перевода"
            val russianShort = "ПП"

            val translations = listOf(
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "en",
                    name = englishName,
                    shortName = englishShort
                ),
                BeMeasureTranslation(
                    id = newMeasureId,
                    locale = "ru",
                    name = russianName,
                    shortName = russianShort
                )
            )
            val request = DbMeasureRequest(measure, translations)

            // Create measure
            val createResponse = repository.newMeasure(request)
            createResponse.isSuccess.shouldBeTrue()

            val createdId = createResponse.result.measure.id

            // Retrieve measure and verify translations
            val retrievedResponse = repository.measure(DbMeasureIdRequest(createdId))

            retrievedResponse.isSuccess.shouldBeTrue()
            retrievedResponse.result.translations.shouldHaveSize(2)

            val enTranslation = retrievedResponse.result.translations.find { it.locale == "en" }
            enTranslation shouldNotBe null
            enTranslation!!.name shouldBe englishName
            enTranslation.shortName shouldBe englishShort

            val ruTranslation = retrievedResponse.result.translations.find { it.locale == "ru" }
            ruTranslation shouldNotBe null
            ruTranslation!!.name shouldBe russianName
            ruTranslation.shortName shouldBe russianShort
        }

        should("handle empty translations list") {
            val measure = BeMeasure(
                id = BeId(UUID.randomUUID().toString()),
                code = "EMPTY_TRANS_${System.currentTimeMillis()}",
                createdAt = Instant.now()
            )
            val request = DbMeasureRequest(measure, emptyList())

            val response = repository.newMeasure(request)

            // Should create measure even with empty translations
            response.isSuccess.shouldBeTrue()
        }
    }
})
