package com.khan366kos.measures.repository

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.UUID
import kotlin.uuid.Uuid

/**
 * Exposed table definition for measures
 */
object MeasuresTable : UUIDTable("measures") {
    val code = varchar("code", 50).uniqueIndex()
    val createdAt = timestamp("created_at")
}
