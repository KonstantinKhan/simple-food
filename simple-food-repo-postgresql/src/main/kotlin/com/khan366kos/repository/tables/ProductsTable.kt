package com.khan366kos.repository.tables

import com.khan366kos.measures.repository.MeasuresTable
import org.jetbrains.exposed.sql.Table

/**
 * Exposed table definition for products
 */
object ProductsTable : Table("products") {
    val id = uuid("id")
    val name = varchar("name", 255)

    // Calories
    val caloriesTitle = varchar("calories_title", 100)
    val caloriesShortTitle = varchar("calories_short_title", 50)
    val caloriesValue = double("calories_value")
    val caloriesMeasureId = uuid("calories_measure_id").references(MeasuresTable.id)

    // Proteins
    val proteinsTitle = varchar("proteins_title", 100)
    val proteinsShortTitle = varchar("proteins_short_title", 50)
    val proteinsValue = double("proteins_value")
    val proteinsMeasureId = uuid("proteins_measure_id").references(MeasuresTable.id)

    // Fats
    val fatsTitle = varchar("fats_title", 100)
    val fatsShortTitle = varchar("fats_short_title", 50)
    val fatsValue = double("fats_value")
    val fatsMeasureId = uuid("fats_measure_id").references(MeasuresTable.id)

    // Carbohydrates
    val carbohydratesTitle = varchar("carbohydrates_title", 100)
    val carbohydratesShortTitle = varchar("carbohydrates_short_title", 50)
    val carbohydratesValue = double("carbohydrates_value")
    val carbohydratesMeasureId = uuid("carbohydrates_measure_id").references(MeasuresTable.id)

    // Weight
    val weightValue = double("weight_value")
    val weightMeasureId = uuid("weight_measure_id").references(MeasuresTable.id)

    // Author
    val authorId = uuid("author_id")
    val authorName = varchar("author_name", 255)
    val authorEmail = varchar("author_email", 255)

    override val primaryKey = PrimaryKey(id)
}
