package com.khan366kos.repository

import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeCategory
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeLocale
import com.khan366kos.common.model.common.BeNutrientShortTitle
import com.khan366kos.common.model.common.BeNutrientTitle
import com.khan366kos.common.model.common.BeNutrientValue
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.common.BeSearchString
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeWeightValue
import com.khan366kos.common.model.measure.BeMeasureName
import com.khan366kos.common.model.measure.BeMeasureShortName
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.product.BeProduct
import com.khan366kos.common.model.product.BeProductName
import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.user.BeAuthorName
import com.khan366kos.common.model.user.BeEmail
import com.khan366kos.common.model.product.repository.DbProductFilterRequest
import com.khan366kos.common.model.product.repository.DbProductIdRequest
import com.khan366kos.common.model.product.repository.DbProductRequest
import com.khan366kos.common.model.product.repository.DbProductResponse
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
            val productNames = products.map { it.productName.value }

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
                productName = BeProductName("Тестовый продукт"),
                productCalories = BeCalories(
                    title = BeNutrientTitle("Калории"),
                    shortTitle = BeNutrientShortTitle("ккал"),
                    value = BeNutrientValue(200.0),
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = BeLocale("ru"),
                        name = BeMeasureName("грамм"),
                        shortName = BeMeasureShortName("г")
                    )
                ),
                productProteins = BeProteins(
                    title = BeNutrientTitle("Белки"),
                    shortTitle = BeNutrientShortTitle("Б"),
                    value = BeNutrientValue(15.0),
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = BeLocale("ru"),
                        name = BeMeasureName("грамм"),
                        shortName = BeMeasureShortName("г")
                    )
                ),
                productFats = BeFats(
                    title = BeNutrientTitle("Жиры"),
                    shortTitle = BeNutrientShortTitle("Ж"),
                    value = BeNutrientValue(5.0),
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = BeLocale("ru"),
                        name = BeMeasureName("грамм"),
                        shortName = BeMeasureShortName("г")
                    )
                ),
                productCarbohydrates = BeCarbohydrates(
                    title = BeNutrientTitle("Углеводы"),
                    shortTitle = BeNutrientShortTitle("У"),
                    value = BeNutrientValue(20.0),
                    measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = BeLocale("ru"),
                        name = BeMeasureName("грамм"),
                        shortName = BeMeasureShortName("г")
                    )
                ),
                weight = BeWeight(
                    value = BeWeightValue(100.0), measure = BeMeasureTranslation(
                        id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                        locale = BeLocale("ru"),
                        name = BeMeasureName("грамм"),
                        shortName = BeMeasureShortName("г")
                    )
                ),
                author = BeAuthor(
                    authorId = BeId(UUID.randomUUID()),
                    name = BeAuthorName("Test Author"),
                    email = BeEmail("test@example.com")
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
            getResponse.result.productName shouldBe BeProductName("Тестовый продукт")
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
            getResponse.result.productName shouldBe BeProductName("Auto-generated ID Test")
            getResponse.result.productId shouldBe response.result.productId
        }
    }

    context("Update product") {
        test("should update existing product successfully") {
            val targetProduct = repository.products().result.first()
            val updatedProduct = targetProduct.copy(productName = BeProductName("Обновленное имя"))

            val request = DbProductRequest(product = updatedProduct)
            val response = repository.updatedProduct(request)

            response.isSuccess shouldBe true
            response.result.productName shouldBe BeProductName("Обновленное имя")

            // Verify update persisted
            val getResponse = repository.product(DbProductIdRequest(id = targetProduct.productId))
            getResponse.result.productName shouldBe BeProductName("Обновленное имя")
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
            val updatedProduct = originalProduct.copy(productName = BeProductName("New Name"))

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
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("Куриная"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe BeProductName("Куриная грудка")
        }

        test("should find product by category") {
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("Мясо"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe BeProductName("Куриная грудка")
        }

        test("should find multiple products with common category") {
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("продукты"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result.size shouldBe 2  // Chicken and rice have "...продукты" in categories
        }

        test("should be case-insensitive search") {
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("РИС"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe BeProductName("Рис белый")
        }

        test("should return empty list for non-matching search") {
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("несуществующий"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result.shouldBeEmpty()
        }

        test("should find product by partial name") {
            val searchRequest = DbProductFilterRequest(searchStr = BeSearchString("масло"))
            val response = repository.foundProducts(searchRequest)

            response.isSuccess shouldBe true
            response.result shouldHaveSize 1
            response.result.first().productName shouldBe BeProductName("Оливковое масло")
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
                locale = BeLocale("ru"),
                name = BeMeasureName("грамм"),
                shortName = BeMeasureShortName("г")
            )

            return BeProduct(
                productId = BeId.NONE,
                productName = BeProductName(name),
                productCalories = BeCalories(
                    title = BeNutrientTitle("Калории"),
                    shortTitle = BeNutrientShortTitle("ккал"),
                    value = BeNutrientValue(100.0),
                    measure = gramMeasure
                ),
                productProteins = BeProteins(
                    title = BeNutrientTitle("Белки"),
                    shortTitle = BeNutrientShortTitle("Б"),
                    value = BeNutrientValue(10.0),
                    measure = gramMeasure
                ),
                productFats = BeFats(
                    title = BeNutrientTitle("Жиры"),
                    shortTitle = BeNutrientShortTitle("Ж"),
                    value = BeNutrientValue(5.0),
                    measure = gramMeasure
                ),
                productCarbohydrates = BeCarbohydrates(
                    title = BeNutrientTitle("Углеводы"),
                    shortTitle = BeNutrientShortTitle("У"),
                    value = BeNutrientValue(10.0),
                    measure = gramMeasure
                ),
                weight = BeWeight(value = BeWeightValue(100.0), measure = gramMeasure),
                author = BeAuthor(
                    authorId = BeId(UUID.randomUUID()),
                    name = BeAuthorName("Test Author"),
                    email = BeEmail("test@example.com")
                ),
                categories = BeCategories(value = listOf(BeCategory("Test")))
            )
        }
    }
}
