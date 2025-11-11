package com.khan366kos.repository

import com.khan366kos.common.model.*
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.simple.*
import java.util.UUID

/**
 * Тестовые данные для демонстрации API
 */
object TestData {

    fun createSampleProducts(): List<BeProduct> {
        // GRAM measure (matches V4 migration: 00000000-0000-0000-0000-000000000001)
        val measureGram = BeMeasureTranslation(
            id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
            locale = "ru",
            name = "грамм",
            shortName = "г"
        )

        // KILOCALORIE measure (matches V4 migration: 00000000-0000-0000-0000-000000000007)
        val measureKcal = BeMeasureTranslation(
            id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
            locale = "ru",
            name = "килокалория",
            shortName = "ккал"
        )

        val product1 = BeProduct(
            productId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")),
            productName = "Куриная грудка",
            productCalories = BeCalories(
                title = "Калорийность",
                shortTitle = "К",
                value = 165.0,
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                value = 31.0,
                measure = measureGram
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                value = 3.6,
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                value = 0.0,
                measure = measureGram
            ),
            weight = BeWeight(value = 100.0, measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = "Admin",
                email = "admin@example.com"
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
            productName = "Рис белый",
            productCalories = BeCalories(
                title = "Калорийность",
                shortTitle = "К",
                value = 365.0,
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                value = 7.5,
                measure = measureGram
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                value = 0.6,
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                value = 79.0,
                measure = measureGram
            ),
            weight = BeWeight(value = 100.0, measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = "Admin",
                email = "admin@example.com"
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
            productName = "Оливковое масло",
            productCalories = BeCalories(
                title = "Калорийность",
                shortTitle = "К",
                value = 884.0,
                measure = measureKcal
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                value = 0.0,
                measure = measureGram
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                value = 100.0,
                measure = measureGram
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                value = 0.0,
                measure = measureGram
            ),
            weight = BeWeight(value = 100.0, measure = measureGram),
            author = BeAuthor(
                authorId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440099")),
                name = "Admin",
                email = "admin@example.com"
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
