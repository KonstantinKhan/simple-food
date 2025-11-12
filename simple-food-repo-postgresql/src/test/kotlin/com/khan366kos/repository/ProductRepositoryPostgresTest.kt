package com.khan366kos.repository

import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeCategory
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.product.BeProduct
import com.khan366kos.common.model.product.repository.DbProductIdRequest
import com.khan366kos.common.model.product.repository.DbProductRequest
import com.khan366kos.measures.repository.MeasuresTable
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.testcontainers.containers.PostgreSQLContainer
import java.util.UUID

class ProductRepositoryPostgresTest : ShouldSpec({
    lateinit var repository: ProductRepositoryPostgres
    lateinit var container: PostgreSQLContainer<*>

    beforeSpec {
        // Start PostgreSQL testcontainer
        container = PostgreSQLContainer("postgres:16")
            .withDatabaseName("simplefood_test")
            .withUsername("test")
            .withPassword("test")
        container.start()

        // Run Flyway migrations
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
        repository = ProductRepositoryPostgres()
    }

    afterSpec {
        container.stop()
    }

    context("newProduct() - Create new product") {
        should("verify database setup and measures exist") {
            val gramId = getGramMeasureId()
            val kcalId = getKcalMeasureId()

            gramId shouldNotBe ""
            kcalId shouldNotBe ""
        }

        should("create a new product successfully") {
            val product = createTestProduct(
                name = "Test Product",
                caloriesValue = 150.0,
                proteinsValue = 20.0,
                fatsValue = 5.0,
                carbohydratesValue = 10.0,
                weightValue = 100.0
            )

            val request = DbProductRequest(product = product)
            val response = repository.newProduct(request)

            response.isSuccess.shouldBeTrue()
            response.result.productId shouldNotBe BeId.NONE
            response.result.productName shouldBe "Test Product"
            response.result.productCalories.value shouldBe 150.0
            response.result.productProteins.value shouldBe 20.0
            response.result.productFats.value shouldBe 5.0
            response.result.productCarbohydrates.value shouldBe 10.0
            response.result.weight.value shouldBe 100.0
        }

        should("generate valid UUID for new product") {
            val product = createTestProduct(name = "UUID Test Product")

            val request = DbProductRequest(product = product)
            val response = repository.newProduct(request)

            response.isSuccess.shouldBeTrue()
            response.result.productId shouldNotBe BeId.NONE

            // Verify it's a valid UUID
            val uuid = UUID.fromString(response.result.productId.value)
            uuid shouldNotBe null
        }

        should("persist product with all nutritional values correctly") {
            val product = createTestProduct(
                name = "Nutritional Test",
                caloriesValue = 250.5,
                proteinsValue = 30.2,
                fatsValue = 10.8,
                carbohydratesValue = 25.3,
                weightValue = 150.0
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.productCalories.value shouldBe 250.5
            retrieved.productCalories.title shouldBe "Калории"
            retrieved.productCalories.shortTitle shouldBe "ккал"

            retrieved.productProteins.value shouldBe 30.2
            retrieved.productProteins.title shouldBe "Белки"
            retrieved.productProteins.shortTitle shouldBe "Б"

            retrieved.productFats.value shouldBe 10.8
            retrieved.productFats.title shouldBe "Жиры"
            retrieved.productFats.shortTitle shouldBe "Ж"

            retrieved.productCarbohydrates.value shouldBe 25.3
            retrieved.productCarbohydrates.title shouldBe "Углеводы"
            retrieved.productCarbohydrates.shortTitle shouldBe "У"

            retrieved.weight.value shouldBe 150.0
        }

        should("persist product with author information") {
            val authorId = UUID.randomUUID()
            val product = createTestProduct(
                name = "Author Test Product",
                authorId = authorId,
                authorName = "John Doe",
                authorEmail = "john.doe@example.com"
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify author
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.author.authorId.value shouldBe authorId.toString()
            retrieved.author.name shouldBe "John Doe"
            retrieved.author.email shouldBe "john.doe@example.com"
        }

        should("persist product with single category") {
            val product = createTestProduct(
                name = "Single Category Product",
                categories = listOf("Мясо")
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify categories
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.categories.value.shouldHaveSize(1)
            retrieved.categories.value.first().value shouldBe "Мясо"
        }

        should("persist product with multiple categories") {
            val product = createTestProduct(
                name = "Multi Category Product",
                categories = listOf("Мясо", "Белковые продукты", "Диетические")
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify categories
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.categories.value.shouldHaveSize(3)

            val categoryValues = retrieved.categories.value.map { it.value }
            categoryValues.shouldContainAll("Мясо", "Белковые продукты", "Диетические")
        }

        should("persist product with empty categories") {
            val product = createTestProduct(
                name = "No Category Product",
                categories = emptyList()
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify categories
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.categories.value.shouldHaveSize(0)
        }

        should("persist product and retrieve it by ID") {
            val uniqueName = "Persistent Product ${System.currentTimeMillis()}"
            val product = createTestProduct(name = uniqueName)

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            val createdId = createResponse.result.productId

            // Retrieve product
            val retrieveResponse = repository.product(DbProductIdRequest(createdId))

            retrieveResponse.isSuccess.shouldBeTrue()
            retrieveResponse.result.productId shouldBe createdId
            retrieveResponse.result.productName shouldBe uniqueName
        }

        should("persist product with measure references") {
            // Use measure IDs from Flyway seed data
            val gramMeasureId = getGramMeasureId()
            val kcalMeasureId = getKcalMeasureId()

            val product = createTestProduct(
                name = "Measure Reference Test",
                measureId = gramMeasureId,
                caloriesMeasureId = kcalMeasureId
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify measures are loaded correctly
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.productCalories.measure.id.value shouldBe kcalMeasureId
            retrieved.productProteins.measure.id.value shouldBe gramMeasureId
            retrieved.productFats.measure.id.value shouldBe gramMeasureId
            retrieved.productCarbohydrates.measure.id.value shouldBe gramMeasureId
            retrieved.weight.measure.id.value shouldBe gramMeasureId
        }

        should("handle product with zero nutritional values") {
            val product = createTestProduct(
                name = "Zero Values Product",
                caloriesValue = 0.0,
                proteinsValue = 0.0,
                fatsValue = 0.0,
                carbohydratesValue = 0.0,
                weightValue = 0.0
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.productCalories.value shouldBe 0.0
            retrieved.productProteins.value shouldBe 0.0
            retrieved.productFats.value shouldBe 0.0
            retrieved.productCarbohydrates.value shouldBe 0.0
            retrieved.weight.value shouldBe 0.0
        }

        should("handle product with large nutritional values") {
            val product = createTestProduct(
                name = "Large Values Product",
                caloriesValue = 9999.99,
                proteinsValue = 999.99,
                fatsValue = 999.99,
                carbohydratesValue = 999.99,
                weightValue = 9999.99
            )

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()

            val retrieved = retrieveResponse.result
            retrieved.productCalories.value shouldBe 9999.99
            retrieved.productProteins.value shouldBe 999.99
            retrieved.productFats.value shouldBe 999.99
            retrieved.productCarbohydrates.value shouldBe 999.99
            retrieved.weight.value shouldBe 9999.99
        }

        should("create multiple products independently") {
            val product1 = createTestProduct(name = "Product 1")
            val product2 = createTestProduct(name = "Product 2")
            val product3 = createTestProduct(name = "Product 3")

            val response1 = repository.newProduct(DbProductRequest(product1))
            val response2 = repository.newProduct(DbProductRequest(product2))
            val response3 = repository.newProduct(DbProductRequest(product3))

            response1.isSuccess.shouldBeTrue()
            response2.isSuccess.shouldBeTrue()
            response3.isSuccess.shouldBeTrue()

            // Verify all have different IDs
            response1.result.productId shouldNotBe response2.result.productId
            response2.result.productId shouldNotBe response3.result.productId
            response1.result.productId shouldNotBe response3.result.productId
        }

        should("handle product with Cyrillic characters in name") {
            val product = createTestProduct(name = "Куриная грудка без кожи")

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()
            retrieveResponse.result.productName shouldBe "Куриная грудка без кожи"
        }

        should("handle product with special characters in name") {
            val product = createTestProduct(name = "Product with (special) [chars] & symbols!")

            val request = DbProductRequest(product = product)
            val createResponse = repository.newProduct(request)
            createResponse.isSuccess.shouldBeTrue()

            // Retrieve and verify
            val retrieveResponse = repository.product(DbProductIdRequest(createResponse.result.productId))
            retrieveResponse.isSuccess.shouldBeTrue()
            retrieveResponse.result.productName shouldBe "Product with (special) [chars] & symbols!"
        }

        should("return failure when measure ID does not exist") {
            val nonExistentMeasureId = UUID.randomUUID().toString()
            val product = createTestProduct(
                name = "Invalid Measure Product",
                measureId = nonExistentMeasureId
            )

            val request = DbProductRequest(product = product)
            val response = repository.newProduct(request)

            // Should fail due to foreign key constraint
            response.isSuccess.shouldBeFalse()
            response.result shouldBe BeProduct.NONE
        }

        should("handle transaction rollback on error") {
            val initialCount = repository.products().result.size

            // Try to create product with invalid measure ID
            val invalidProduct = createTestProduct(
                name = "Invalid Product",
                measureId = UUID.randomUUID().toString()
            )

            val response = repository.newProduct(DbProductRequest(invalidProduct))
            response.isSuccess.shouldBeFalse()

            // Verify count hasn't changed
            val finalCount = repository.products().result.size
            finalCount shouldBe initialCount
        }
    }
}) {
    companion object {
        // Helper function to get measure IDs from database
        fun getGramMeasureId(): String {
            return transaction {
                MeasuresTable.selectAll()
                    .where { MeasuresTable.code eq "GRAM" }
                    .single()[MeasuresTable.id]
                    .toString()
            }
        }

        fun getKcalMeasureId(): String {
            return transaction {
                MeasuresTable.selectAll()
                    .where { MeasuresTable.code eq "KILOCALORIE" }
                    .single()[MeasuresTable.id]
                    .toString()
            }
        }

        fun createTestProduct(
            name: String = "Test Product",
            caloriesValue: Double = 100.0,
            proteinsValue: Double = 10.0,
            fatsValue: Double = 5.0,
            carbohydratesValue: Double = 15.0,
            weightValue: Double = 100.0,
            measureId: String? = null,
            caloriesMeasureId: String? = null,
            authorId: UUID = UUID.randomUUID(),
            authorName: String = "Test Author",
            authorEmail: String = "test@example.com",
            categories: List<String> = listOf("Test Category")
        ): BeProduct {
            val gramId = measureId ?: getGramMeasureId()
            val kcalId = caloriesMeasureId ?: getKcalMeasureId()
            val gramMeasure = BeMeasureTranslation(
                id = BeId(gramId),
                locale = "ru",
                name = "грамм",
                shortName = "г"
            )

            val kcalMeasure = BeMeasureTranslation(
                id = BeId(kcalId),
                locale = "ru",
                name = "килокалория",
                shortName = "ккал"
            )

            return BeProduct(
                productId = BeId.NONE,
                productName = name,
                productCalories = BeCalories(
                    title = "Калории",
                    shortTitle = "ккал",
                    value = caloriesValue,
                    measure = kcalMeasure
                ),
                productProteins = BeProteins(
                    title = "Белки",
                    shortTitle = "Б",
                    value = proteinsValue,
                    measure = gramMeasure
                ),
                productFats = BeFats(
                    title = "Жиры",
                    shortTitle = "Ж",
                    value = fatsValue,
                    measure = gramMeasure
                ),
                productCarbohydrates = BeCarbohydrates(
                    title = "Углеводы",
                    shortTitle = "У",
                    value = carbohydratesValue,
                    measure = gramMeasure
                ),
                weight = BeWeight(
                    value = weightValue,
                    measure = gramMeasure
                ),
                author = BeAuthor(
                    authorId = BeId(authorId),
                    name = authorName,
                    email = authorEmail
                ),
                categories = BeCategories(categories.map { BeCategory(it) })
            )
        }
    }
}
