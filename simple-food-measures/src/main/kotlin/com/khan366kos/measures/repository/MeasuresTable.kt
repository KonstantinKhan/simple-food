package com.khan366kos.measures.repository

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * Exposed table definition for measures
 */
object MeasuresTable : Table("measures") {
    val id = uuid("id")
    val code = varchar("code", 50).uniqueIndex()
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}
