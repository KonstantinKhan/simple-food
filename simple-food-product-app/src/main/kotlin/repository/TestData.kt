package com.khan366kos.repository

import com.khan366kos.common.model.*
import com.khan366kos.common.model.simple.*
import java.util.UUID

/**
 * Тестовые данные для демонстрации API
 */
object TestData {
    
    fun createSampleProducts(): List<BeProduct> {
        val measure100g = BeMeasure(measureName = "грамм", measureShortName = "г")
        
        val product1 = BeProduct(
            productId = BeId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")),
            productName = "Куриная грудка",
            productCalories = BeCalories(
                title = "Калории",
                shortTitle = "ккал",
                measure = measure100g
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                measure = measure100g
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                measure = measure100g
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                measure = measure100g
            ),
            weight = BeWeight(value = 100.0, measure = measure100g),
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
                title = "Калории",
                shortTitle = "ккал",
                measure = measure100g
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                measure = measure100g
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                measure = measure100g
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                measure = measure100g
            ),
            weight = BeWeight(value = 100.0, measure = measure100g),
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
                title = "Калории",
                shortTitle = "ккал",
                measure = measure100g
            ),
            productProteins = BeProteins(
                title = "Белки",
                shortTitle = "Б",
                measure = measure100g
            ),
            productFats = BeFats(
                title = "Жиры",
                shortTitle = "Ж",
                measure = measure100g
            ),
            productCarbohydrates = BeCarbohydrates(
                title = "Углеводы",
                shortTitle = "У",
                measure = measure100g
            ),
            weight = BeWeight(value = 100.0, measure = measure100g),
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

