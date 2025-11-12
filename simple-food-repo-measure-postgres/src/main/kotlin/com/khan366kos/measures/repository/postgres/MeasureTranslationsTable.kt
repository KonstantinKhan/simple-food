package com.khan366kos.measures.repository.postgres

import org.jetbrains.exposed.sql.Table

/**
 * Exposed table definition for measure translations
 */
object MeasureTranslationsTable : Table("measure_translations") {
    val measureId = uuid("measure_id").references(MeasuresTable.id)
    val locale = varchar("locale", 10)
    val measureName = varchar("measure_name", 255)
    val measureShortName = varchar("measure_short_name", 50)

    override val primaryKey = PrimaryKey(measureId, locale)
}
