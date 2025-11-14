package com.khan366kos.repository

import com.khan366kos.common.model.*
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeCategory
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeWeightValue
import com.khan366kos.common.model.common.BeNutrientTitle
import com.khan366kos.common.model.common.BeNutrientShortTitle
import com.khan366kos.common.model.common.BeNutrientValue
import com.khan366kos.common.model.common.BeLocale
import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.user.BeAuthorName
import com.khan366kos.common.model.user.BeEmail
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasureName
import com.khan366kos.common.model.measure.BeMeasureShortName
import com.khan366kos.common.model.product.BeProduct
import com.khan366kos.common.model.product.BeProductName
import java.util.UUID

/**
 * Тестовые данные для демонстрации API
 */
object TestData {

    fun createSampleProducts(): List<BeProduct> {
        // GRAM measure (matches V4 migration: 00000000-0000-0000-0000-000000000001)
        val measureGram = BeMeasureTranslation(
            id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
            locale = BeLocale("ru"),
            name = BeMeasureName("грамм"),
            shortName = BeMeasureShortName("г")
        )

        // KILOCALORIE measure (matches V4 migration: 00000000-0000-0000-0000-000000000007)
        val measureKcal = BeMeasureTranslation(
            id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
            locale = BeLocale("ru"),
            name = BeMeasureName("килокалория"),
            shortName = BeMeasureShortName("ккал")
        )

        val product1 = BeProduct(
            productId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")),
            productName = BeProductName("Куриная грудка"),
            productCalories = BeCalories(
                title = BeNutrientTitle("Калорийность"),
                shortTitle = BeNutrientShortTitle("К"),
                value = BeNutrientValue(165.0),
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = BeNutrientTitle("Белки"),
                shortTitle = BeNutrientShortTitle("Б"),
                value = BeNutrientValue(31.0),
                measure = measureGram
            ),
            productFats = BeFats(
                title = BeNutrientTitle("Жиры"),
                shortTitle = BeNutrientShortTitle("Ж"),
                value = BeNutrientValue(3.6),
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = BeNutrientTitle("Углеводы"),
                shortTitle = BeNutrientShortTitle("У"),
                value = BeNutrientValue(0.0),
                measure = measureGram
            ),
            weight = BeWeight(value = BeWeightValue(100.0), measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = BeAuthorName("Admin"),
                email = BeEmail("admin@example.com")
            ),
            categories = BeCategories(
                value = listOf(
                    BeCategory("Мясо"),
                    BeCategory("Птица"),
                    BeCategory("Белковые продукты")
                )
            )
        )

        val product2 = BeProduct(
            productId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440002")),
            productName = BeProductName("Рис белый"),
            productCalories = BeCalories(
                title = BeNutrientTitle("Калорийность"),
                shortTitle = BeNutrientShortTitle("К"),
                value = BeNutrientValue(365.0),
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = BeNutrientTitle("Белки"),
                shortTitle = BeNutrientShortTitle("Б"),
                value = BeNutrientValue(7.5),
                measure = measureGram
            ),
            productFats = BeFats(
                title = BeNutrientTitle("Жиры"),
                shortTitle = BeNutrientShortTitle("Ж"),
                value = BeNutrientValue(0.6),
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = BeNutrientTitle("Углеводы"),
                shortTitle = BeNutrientShortTitle("У"),
                value = BeNutrientValue(79.0),
                measure = measureGram
            ),
            weight = BeWeight(value = BeWeightValue(100.0), measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = BeAuthorName("Admin"),
                email = BeEmail("admin@example.com")
            ),
            categories = BeCategories(
                value = listOf(
                    BeCategory("Крупы"),
                    BeCategory("Гарнир"),
                    BeCategory("Углеводные продукты")
                )
            )
        )

        val product3 = BeProduct(
            productId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440003")),
            productName = BeProductName("Оливковое масло"),
            productCalories = BeCalories(
                title = BeNutrientTitle("Калорийность"),
                shortTitle = BeNutrientShortTitle("К"),
                value = BeNutrientValue(884.0),
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = BeNutrientTitle("Белки"),
                shortTitle = BeNutrientShortTitle("Б"),
                value = BeNutrientValue(0.0),
                measure = measureGram
            ),
            productFats = BeFats(
                title = BeNutrientTitle("Жиры"),
                shortTitle = BeNutrientShortTitle("Ж"),
                value = BeNutrientValue(100.0),
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = BeNutrientTitle("Углеводы"),
                shortTitle = BeNutrientShortTitle("У"),
                value = BeNutrientValue(0.0),
                measure = measureGram
            ),
            weight = BeWeight(value = BeWeightValue(100.0), measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = BeAuthorName("Admin"),
                email = BeEmail("admin@example.com")
            ),
            categories = BeCategories(
                value = listOf(
                    BeCategory("Масла"),
                    BeCategory("Жиры")
                )
            )
        )

        return listOf(product1, product2, product3)
    }
}
