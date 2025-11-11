package com.khan366kos.repository

import com.khan366kos.common.model.*
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.repository.*
import com.khan366kos.common.model.simple.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldBeEmpty
import java.util.UUID

class ProductRepositoryTest : FunSpec({

    lateinit var repository: ProductRepository

    beforeTest {
        repository = ProductRepository()
    }

    context("ProductRepository initialization") {
        test("should initialize with 3 sample products") {
            val response = repository.products()
            response.isSuccess shouldBe true
            response.result shouldHaveSize 3
        }

        test("should load sample products with expected names") {
            val response = repository.products()
            val products = response.result
            val productNames = products.map { it.productName }

            productNames.shouldContain("Куриная грудка")
            productNames.shouldContain("Рис белый")
            productNames.shouldContain("Оливковое масло")
        }
    }

    context("Get all products") {
        test("should return all products with success flag") {
            val response = repository.products()

            response.isSuccess shouldBe true
            response.result shouldHaveSize 3
        }

        test("should return products with complete data") {
            val response = repository.products()
            val product = response.result.first()

            product.productName shouldNotBe null
            product.productCalories shouldNotBe null
            product.productProteins shouldNotBe null
            product.productFats shouldNotBe null
            product.productCarbohydrates shouldNotBe null
            product.weight shouldNotBe null
            product.author shouldNotBe null
            product.categories shouldNotBe null
        }
    }

    context("Get product by ID") {
        test("should return existing product") {
            val allProducts = repository.products().result
            val targetProduct = allProducts.first()

            val request = DbProductIdRequest(id = targetProduct.productId)
            val response = repository.product(request)

            response.isSuccess shouldBe true
            response.result.productId shouldBe targetProduct.productId
            response.result.productName shouldBe targetProduct.productName
        }

        test("should return failure for non-existent product") {
            val nonExistentId = BeId(UUID.randomUUID())
            val request = DbProductIdRequest(id = nonExistentId)
            val response = repository.product(request)

            response.isSuccess shouldBe false
            response.result shouldBe BeProduct.NONE
        }
    }

    context("Create new product") {
        test("should add new product successfully") {
            val newProduct = BeProduct(
                productId = BeId.NONE,
                productName = "Тестовый продукт",
                productCalories = BeCalories(
                    title = "Калории",
                    shortTitle = "ккал",
                    value = 200.0,
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = "ru",
                        name = "грамм",
                        shortName = "г"
                    )
                ),
                productProteins = BeProteins(
                    title = "Белки",
                    shortTitle = "Б",
                    value = 15.0,
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = "ru",
                        name = "грамм",
                        shortName = "г"
                    )
                ),
                productFats = BeFats(
                    title = "Жиры",
                    shortTitle = "Ж",
                    value = 5.0,
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = "ru",
                        name = "грамм",
                        shortName = "г"
                    )
                ),
                productCarbohydrates = BeCarbohydrates(
                    title = "Углеводы",
                    shortTitle = "У",
                    value = 20.0,
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = "ru",
                        name = "грамм",
                        shortName = "г"
                    )
                ),
                weight = BeWeight(value = 100.0, measure = BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = "ru",
                    name = "грамм",
                    shortName = "г"
                )
                ),
                author = BeAuthor(
                    authorId = BeId(UUID.randomUUID()),
                    name = "Test Author",
                    email = "test@example.com"
                ),
                categories = BeCategories(value = listOf(BeCategory("Тест")))
            )

            val request = DbProductRequest(product = newProduct)
            val response = repository.newProduct(request)

            response.isSuccess shouldBe true
            response.result.productId shouldNotBe BeId.NONE
            // Verify it's a valid UUID
            UUID.fromString(response.result.productId.value)

            // Verify product is stored with the generated ID
            val getResponse = repository.product(DbProductIdRequest(id = response.result.productId))
            getResponse.isSuccess shouldBe true
            getResponse.result.productName shouldBe "Тестовый продукт"
        }

        test("should increase product count after adding new product") {
            val initialCount = repository.products().result.size

            val newProduct = createTestProduct()
            repository.newProduct(DbProductRequest(product = newProduct))

            val finalCount = repository.products().result.size
            finalCount shouldBe initialCount + 1
        }

        test("should auto-generate UUID when creating product") {
            val newProduct = createTestProduct("Auto-generated ID Test")

            // Verify input has no ID
            newProduct.productId shouldBe BeId.NONE

            val response = repository.newProduct(DbProductRequest(product = newProduct))

            response.isSuccess shouldBe true
            // Verify returned product has a generated ID
            response.result.productId shouldNotBe BeId.NONE
            response.result.productId.value shouldNotBe ""

            // Verify it's a valid UUID format
            val generatedUuid = UUID.fromString(response.result.productId.value)
            generatedUuid shouldNotBe null

            // Verify product can be retrieved by generated ID
            val getResponse = repository.product(DbProductIdRequest(id = response.result.productId))
            getResponse.isSuccess shouldBe true
            getResponse.result.productName shouldBe "Auto-generated ID Test"
            getResponse.result.productId shouldBe response.result.productId
        }
    }

    context("Update product") {
        test("should update existing product successfully") {
            val targetProduct = repository.products().result.first()
            val updatedProduct = targetProduct.copy(productName = "Обновленное имя")

            val request = DbProductRequest(product = updatedProduct)
            val response = repository.updatedProduct(request)

            response.isSuccess shouldBe true
            response.result.productName shouldBe "Обновленное имя"

            // Verify update persisted
            val getResponse = repository.product(DbProductIdRequest(id = targetProduct.productId))
            getResponse.result.productName shouldBe "Обновленное имя"
        }

        test("should fail updating non-existent product") {
            val nonExistentProduct = createTestProduct()

            val request = DbProductRequest(product = nonExistentProduct)
            val response = repository.updatedProduct(request)

            response.isSuccess shouldBe false
            response.result shouldBe BeProduct.NONE
        }

        test("should preserve product data on update") {
            val originalProduct = repository.products().result.first()
            val updatedProduct = originalProduct.copy(productName = "New Name")

            repository.updatedProduct(DbProductRequest(product = updatedProduct))

            val retrieved = repository.product(DbProductIdRequest(id = originalProduct.productId))
            retrieved.result.author shouldBe originalProduct.author
            retrieved.result.categories shouldBe originalProduct.categories
        }
    }

    context("Delete product") {
        test("should delete existing product successfully") {
            val targetProduct = repository.products().result.first()
            val initialCount = repository.products().result.size

            val request = DbProductIdRequest(id = targetProduct.productId)
            val response = repository.deletedProduct(request)

            response.isSuccess shouldBe true
            response.result.productId shouldBe targetProduct.productId

            // Verify product is removed
            val finalCount = repository.products().result.size
            finalCount shouldBe initialCount - 1
        }

        test("should fail deleting non-existent product") {
            val nonExistentId = BeId(UUID.randomUUID())

            val request = DbProductIdRequest(id = nonExistentId)
            val response = repository.deletedProduct(request)

            response.isSuccess shouldBe false
            response.result shouldBe BeProduct.NONE
        }

        test("should not be retrievable after deletion") {
            val targetProduct = repository.products().result.first()
            val productId = targetProduct.productId

            repository.deletedProduct(DbProductIdRequest(id = productId))

            val response = repository.product(DbProductIdRequest(id = productId))
            response.isSuccess shouldBe false
        }
    }

    context("Search products") {
        test("should find product by name") {
            val searchRequest = DbProductFilterRequest(searchStr = "Куриная")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe "Куриная грудка"
        }

        test("should find product by category") {
            val searchRequest = DbProductFilterRequest(searchStr = "Мясо")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe "Куриная грудка"
        }

        test("should find multiple products with common category") {
            val searchRequest = DbProductFilterRequest(searchStr = "продукты")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result.size shouldBe 2  // Chicken and rice have "...продукты" in categories
        }

        test("should be case-insensitive search") {
            val searchRequest = DbProductFilterRequest(searchStr = "РИС")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe "Рис белый"
        }

        test("should return empty list for non-matching search") {
            val searchRequest = DbProductFilterRequest(searchStr = "несуществующий")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result.shouldBeEmpty()
        }

        test("should find product by partial name") {
            val searchRequest = DbProductFilterRequest(searchStr = "масло")
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe "Оливковое масло"
        }
    }

    context("Thread-safety operations") {
        test("should handle concurrent reads safely") {
            val threads = (1..10).map {
                Thread {
                    val response = repository.products()
                    response.result shouldHaveSize 3
                }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }
        }

        test("should handle concurrent writes safely") {
            val responses = mutableListOf<DbProductResponse>()
            val threads = (1..5).map { index ->
                Thread {
                    val newProduct = createTestProduct("Product $index")
                    val response = repository.newProduct(DbProductRequest(product = newProduct))
                    synchronized(responses) {
                        responses.add(response)
                    }
                }
            }
            threads.forEach { it.start() }
            threads.forEach { it.join() }

            val finalCount = repository.products().result.size
            finalCount shouldBe 8  // 3 initial + 5 new

            // Verify all products have generated IDs
            responses.forEach { response ->
                response.result.productId shouldNotBe BeId.NONE
                response.result.productId.value shouldNotBe ""
            }

            // Verify all generated IDs are unique
            val generatedIds = responses.map { it.result.productId }.toSet()
            generatedIds shouldHaveSize 5
        }
    }

}) {
    companion object {
        fun createTestProduct(name: String = "Test Product"): BeProduct {
            val gramMeasure = BeMeasureTranslation(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                locale = "ru",
                name = "грамм",
                shortName = "г"
            )

            return BeProduct(
                productId = BeId.NONE,
                productName = name,
                productCalories = BeCalories(
                    title = "Калории",
                    shortTitle = "ккал",
                    value = 100.0,
                    measure = gramMeasure
                ),
                productProteins = BeProteins(
                    title = "Белки",
                    shortTitle = "Б",
                    value = 10.0,
                    measure = gramMeasure
                ),
                productFats = BeFats(
                    title = "Жиры",
                    shortTitle = "Ж",
                    value = 5.0,
                    measure = gramMeasure
                ),
                productCarbohydrates = BeCarbohydrates(
                    title = "Углеводы",
                    shortTitle = "У",
                    value = 10.0,
                    measure = gramMeasure
                ),
                weight = BeWeight(value = 100.0, measure = gramMeasure),
                author = BeAuthor(
                    authorId = BeId(UUID.randomUUID()),
                    name = "Test Author",
                    email = "test@example.com"
                ),
                categories = BeCategories(value = listOf(BeCategory("Test")))
            )
        }
    }
}
