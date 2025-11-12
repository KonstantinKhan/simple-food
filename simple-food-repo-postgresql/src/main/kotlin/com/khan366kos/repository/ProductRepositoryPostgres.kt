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
import com.khan366kos.common.model.product.repository.DbProductFilterRequest
import com.khan366kos.common.model.product.repository.DbProductIdRequest
import com.khan366kos.common.model.product.repository.DbProductRequest
import com.khan366kos.common.model.product.repository.DbProductResponse
import com.khan366kos.common.model.product.repository.DbProductsResponse
import com.khan366kos.common.model.product.repository.IRepoProduct
import com.khan366kos.measures.repository.postgres.MeasuresTable
import com.khan366kos.measures.repository.postgres.MeasureTranslationsTable
import com.khan366kos.repository.tables.ProductCategoriesTable
import com.khan366kos.repository.tables.ProductsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

/**
 * PostgreSQL implementation of product repository using Exposed DSL
 */
class ProductRepositoryPostgres : IRepoProduct {

    override fun products(): DbProductsResponse {
        return transaction {
            try {
                val products = ProductsTable.selectAll().map { row ->
                    rowToBeProduct(row)
                }
                DbProductsResponse(result = products, isSuccess = true)
            } catch (e: Exception) {
                DbProductsResponse(result = emptyList(), isSuccess = false)
            }
        }
    }

    override fun product(request: DbProductIdRequest): DbProductResponse {
        return transaction {
            try {
                val uuid = UUID.fromString(request.id.value)
                val row = ProductsTable.selectAll().where { ProductsTable.id eq uuid }.singleOrNull()
                if (row != null) {
                    DbProductResponse(result = rowToBeProduct(row), isSuccess = true)
                } else {
                    DbProductResponse(result = BeProduct.NONE, isSuccess = false)
                }
            } catch (e: Exception) {
                DbProductResponse(result = BeProduct.NONE, isSuccess = false)
            }
        }
    }

    override fun newProduct(request: DbProductRequest): DbProductResponse {
        return transaction {
            try {
                val product = request.product

                // Insert product and get the generated UUID
                val insertedEntityId = ProductsTable.insertAndGetId {
                    it[name] = product.productName

                    // Calories
                    it[caloriesTitle] = product.productCalories.title
                    it[caloriesShortTitle] = product.productCalories.shortTitle
                    it[caloriesValue] = product.productCalories.value
                    it[caloriesMeasureId] = UUID.fromString(product.productCalories.measure.id.value)

                    // Proteins
                    it[proteinsTitle] = product.productProteins.title
                    it[proteinsShortTitle] = product.productProteins.shortTitle
                    it[proteinsValue] = product.productProteins.value
                    it[proteinsMeasureId] = UUID.fromString(product.productProteins.measure.id.value)

                    // Fats
                    it[fatsTitle] = product.productFats.title
                    it[fatsShortTitle] = product.productFats.shortTitle
                    it[fatsValue] = product.productFats.value
                    it[fatsMeasureId] = UUID.fromString(product.productFats.measure.id.value)

                    // Carbohydrates
                    it[carbohydratesTitle] = product.productCarbohydrates.title
                    it[carbohydratesShortTitle] = product.productCarbohydrates.shortTitle
                    it[carbohydratesValue] = product.productCarbohydrates.value
                    it[carbohydratesMeasureId] = UUID.fromString(product.productCarbohydrates.measure.id.value)

                    // Weight
                    it[weightValue] = product.weight.value
                    it[weightMeasureId] = UUID.fromString(product.weight.measure.id.value)

                    // Author
                    it[authorId] = UUID.fromString(product.author.authorId.value)
                    it[authorName] = product.author.name
                    it[authorEmail] = product.author.email
                }

                val insertedId = insertedEntityId.value

                // Insert categories
                product.categories.value.forEach { category ->
                    ProductCategoriesTable.insert {
                        it[productId] = insertedId
                        it[ProductCategoriesTable.category] = category.value
                    }
                }

                DbProductResponse(result = product.copy(productId = BeId(insertedId)), isSuccess = true)
            } catch (e: Exception) {
                DbProductResponse(result = BeProduct.NONE, isSuccess = false)
            }
        }
    }

    override fun updatedProduct(request: DbProductRequest): DbProductResponse {
        return transaction {
            try {
                val product = request.product
                val productUuid = UUID.fromString(product.productId.value)

                // Check if product exists
                val exists = ProductsTable.selectAll().where { ProductsTable.id eq productUuid }.count() > 0
                if (!exists) {
                    return@transaction DbProductResponse(result = BeProduct.NONE, isSuccess = false)
                }

                // Update product
                ProductsTable.update({ ProductsTable.id eq productUuid }) {
                    it[name] = product.productName

                    // Calories
                    it[caloriesTitle] = product.productCalories.title
                    it[caloriesShortTitle] = product.productCalories.shortTitle
                    it[caloriesValue] = product.productCalories.value
                    it[caloriesMeasureId] = UUID.fromString(product.productCalories.measure.id.value)

                    // Proteins
                    it[proteinsTitle] = product.productProteins.title
                    it[proteinsShortTitle] = product.productProteins.shortTitle
                    it[proteinsValue] = product.productProteins.value
                    it[proteinsMeasureId] = UUID.fromString(product.productProteins.measure.id.value)

                    // Fats
                    it[fatsTitle] = product.productFats.title
                    it[fatsShortTitle] = product.productFats.shortTitle
                    it[fatsValue] = product.productFats.value
                    it[fatsMeasureId] = UUID.fromString(product.productFats.measure.id.value)

                    // Carbohydrates
                    it[carbohydratesTitle] = product.productCarbohydrates.title
                    it[carbohydratesShortTitle] = product.productCarbohydrates.shortTitle
                    it[carbohydratesValue] = product.productCarbohydrates.value
                    it[carbohydratesMeasureId] = UUID.fromString(product.productCarbohydrates.measure.id.value)

                    // Weight
                    it[weightValue] = product.weight.value
                    it[weightMeasureId] = UUID.fromString(product.weight.measure.id.value)

                    // Author
                    it[authorId] = UUID.fromString(product.author.authorId.value)
                    it[authorName] = product.author.name
                    it[authorEmail] = product.author.email
                }

                // Delete old categories and insert new ones
                ProductCategoriesTable.deleteWhere { productId eq productUuid }
                product.categories.value.forEach { category ->
                    ProductCategoriesTable.insert {
                        it[ProductCategoriesTable.productId] = productUuid
                        it[ProductCategoriesTable.category] = category.value
                    }
                }

                DbProductResponse(result = product, isSuccess = true)
            } catch (e: Exception) {
                DbProductResponse(result = BeProduct.NONE, isSuccess = false)
            }
        }
    }

    override fun deletedProduct(request: DbProductIdRequest): DbProductResponse {
        return transaction {
            try {
                val productUuid = UUID.fromString(request.id.value)

                // Get product before deletion
                val row = ProductsTable.selectAll().where { ProductsTable.id eq productUuid }.singleOrNull()
                if (row == null) {
                    return@transaction DbProductResponse(result = BeProduct.NONE, isSuccess = false)
                }

                val product = rowToBeProduct(row)

                // Delete product (categories will be deleted automatically due to CASCADE)
                ProductsTable.deleteWhere { id eq productUuid }

                DbProductResponse(result = product, isSuccess = true)
            } catch (e: Exception) {
                DbProductResponse(result = BeProduct.NONE, isSuccess = false)
            }
        }
    }

    override fun foundProducts(request: DbProductFilterRequest): DbProductsResponse {
        return transaction {
            try {
                val searchStr = request.searchStr.lowercase()

                // Search in product name
                val productIds = ProductsTable
                    .selectAll()
                    .where { ProductsTable.name.lowerCase() like "%$searchStr%" }
                    .map { it[ProductsTable.id].value }
                    .toSet()

                // Search in categories
                val categoryProductIds = ProductCategoriesTable
                    .selectAll()
                    .where { ProductCategoriesTable.category.lowerCase() like "%$searchStr%" }
                    .map { it[ProductCategoriesTable.productId] }
                    .toSet()

                // Combine both sets
                val allProductIds = productIds + categoryProductIds

                // Get all matching products
                val products = if (allProductIds.isNotEmpty()) {
                    ProductsTable
                        .selectAll()
                        .where { ProductsTable.id inList allProductIds }
                        .map { row -> rowToBeProduct(row) }
                } else {
                    emptyList()
                }

                DbProductsResponse(result = products, isSuccess = true)
            } catch (e: Exception) {
                DbProductsResponse(result = emptyList(), isSuccess = false)
            }
        }
    }

    /**
     * Load measure from database by ID with translation
     */
    private fun loadMeasure(measureId: UUID, locale: String = "ru"): BeMeasureTranslation {
        val measureRow = MeasuresTable.selectAll()
            .where { MeasuresTable.id eq measureId }
            .singleOrNull() ?: return BeMeasureTranslation.NONE

        val translationRow = MeasureTranslationsTable.selectAll()
            .where {
                (MeasureTranslationsTable.measureId eq measureId) and
                        (MeasureTranslationsTable.locale eq locale)
            }
            .singleOrNull()

        // If no translation for requested locale, try to get any translation
        val translation = translationRow ?: MeasureTranslationsTable.selectAll()
            .where { MeasureTranslationsTable.measureId eq measureId }
            .firstOrNull()

        return if (translation != null) {
            BeMeasureTranslation(
                id = BeId(measureRow[MeasuresTable.id].value),
                locale = translation[MeasureTranslationsTable.locale],
                name = translation[MeasureTranslationsTable.measureName],
                shortName = translation[MeasureTranslationsTable.measureShortName]
            )
        } else {
            BeMeasureTranslation(
                id = BeId(measureRow[MeasuresTable.id].value),
                locale = locale,
                name = "",
                shortName = ""
            )
        }
    }

    /**
     * Convert database row to BeProduct
     */
    private fun rowToBeProduct(row: ResultRow): BeProduct {
        val productId = row[ProductsTable.id].value

        // Get categories for this product
        val categories = ProductCategoriesTable
            .selectAll()
            .where { ProductCategoriesTable.productId eq productId }
            .map { BeCategory(it[ProductCategoriesTable.category]) }

        // Load measures
        val caloriesMeasure = loadMeasure(row[ProductsTable.caloriesMeasureId])
        val proteinsMeasure = loadMeasure(row[ProductsTable.proteinsMeasureId])
        val fatsMeasure = loadMeasure(row[ProductsTable.fatsMeasureId])
        val carbohydratesMeasure = loadMeasure(row[ProductsTable.carbohydratesMeasureId])
        val weightMeasure = loadMeasure(row[ProductsTable.weightMeasureId])

        return BeProduct(
            productId = BeId(productId),
            productName = row[ProductsTable.name],
            productCalories = BeCalories(
                title = row[ProductsTable.caloriesTitle],
                shortTitle = row[ProductsTable.caloriesShortTitle],
                value = row[ProductsTable.caloriesValue],
                measure = caloriesMeasure
            ),
            productProteins = BeProteins(
                title = row[ProductsTable.proteinsTitle],
                shortTitle = row[ProductsTable.proteinsShortTitle],
                value = row[ProductsTable.proteinsValue],
                measure = proteinsMeasure
            ),
            productFats = BeFats(
                title = row[ProductsTable.fatsTitle],
                shortTitle = row[ProductsTable.fatsShortTitle],
                value = row[ProductsTable.fatsValue],
                measure = fatsMeasure
            ),
            productCarbohydrates = BeCarbohydrates(
                title = row[ProductsTable.carbohydratesTitle],
                shortTitle = row[ProductsTable.carbohydratesShortTitle],
                value = row[ProductsTable.carbohydratesValue],
                measure = carbohydratesMeasure
            ),
            weight = BeWeight(
                value = row[ProductsTable.weightValue],
                measure = weightMeasure
            ),
            author = BeAuthor(
                authorId = BeId(row[ProductsTable.authorId]),
                name = row[ProductsTable.authorName],
                email = row[ProductsTable.authorEmail]
            ),
            categories = BeCategories(categories)
        )
    }
}
