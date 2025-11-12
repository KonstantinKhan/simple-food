package com.khan366kos.measures.repository

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasureWithTranslations
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import java.time.Instant

/**
 * PostgreSQL implementation of measure repository using Exposed DSL
 */
class MeasureRepositoryPostgres : IRepoMeasure {

    override fun measures(): DbMeasuresResponse {
        return transaction {
            try {
                val measuresWithTranslations = loadAllMeasuresWithTranslations()
                DbMeasuresResponse(result = measuresWithTranslations, isSuccess = true)
            } catch (e: Exception) {
                DbMeasuresResponse(result = emptyList(), isSuccess = false)
            }
        }
    }

    override fun foundMeasures(request: DbMeasureFilterRequest): DbMeasuresResponse {
        return transaction {
            try {
                val allMeasures = loadAllMeasuresWithTranslations()

                val filtered = allMeasures.filter { measureWithTranslations ->
                    // Filter by locale if specified
                    val localeMatch = request.locale?.let { locale ->
                        measureWithTranslations.translations.any { it.locale == locale }
                    } ?: true

                    // Filter by search text if specified
                    val searchMatch = request.searchText?.let { searchText ->
                        val lower = searchText.lowercase()
                        measureWithTranslations.measure.code.lowercase().contains(lower) ||
                                measureWithTranslations.translations.any { translation ->
                                    translation.name.lowercase().contains(lower) ||
                                            translation.shortName.lowercase().contains(lower)
                                }
                    } ?: true

                    localeMatch && searchMatch
                }

                DbMeasuresResponse(result = filtered, isSuccess = true)
            } catch (e: Exception) {
                DbMeasuresResponse(result = emptyList(), isSuccess = false)
            }
        }
    }

    override fun measure(request: DbMeasureIdRequest): DbMeasureResponse {
        return transaction {
            try {
                val uuid = UUID.fromString(request.id.value)
                val measureRow = MeasuresTable.selectAll().where { MeasuresTable.id eq uuid }.singleOrNull()

                if (measureRow != null) {
                    val measure = rowToBeMeasure(measureRow)
                    val translations = loadTranslationsForMeasure(measure.id)
                    DbMeasureResponse(
                        result = BeMeasureWithTranslations(measure, translations),
                        isSuccess = true
                    )
                } else {
                    DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }
            } catch (e: Exception) {
                DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
            }
        }
    }

    override fun measureByCode(request: DbMeasureCodeRequest): DbMeasureResponse {
        return transaction {
            try {
                val measureRow = MeasuresTable.selectAll().where { MeasuresTable.code eq request.code }.singleOrNull()

                if (measureRow != null) {
                    val measure = rowToBeMeasure(measureRow)
                    val translations = loadTranslationsForMeasure(measure.id)
                    DbMeasureResponse(
                        result = BeMeasureWithTranslations(measure, translations),
                        isSuccess = true
                    )
                } else {
                    DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }
            } catch (e: Exception) {
                DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
            }
        }
    }

    override fun newMeasure(request: DbMeasureRequest): DbMeasureResponse {
        return transaction {
            try {
                val measure = request.measure

                // Check if code already exists
                val existingCode = MeasuresTable.selectAll().where { MeasuresTable.code eq measure.code }.count()
                if (existingCode > 0) {
                    return@transaction DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }
                // Insert measure - let database generate ID and createdAt
                val insertedId = MeasuresTable.insertAndGetId {
                    it[code] = measure.code
                    // id and createdAt use database defaults
                }

                // Insert translations with generated ID
                request.translations.forEach { translation ->
                    MeasureTranslationsTable.insert {
                        it[measureId] = insertedId.value
                        it[locale] = translation.locale
                        it[measureName] = translation.name
                        it[measureShortName] = translation.shortName
                    }
                }

                // Fetch and return the created measure with database-generated values
                val createdMeasure = measure(DbMeasureIdRequest(id = BeId(insertedId.value)))
                createdMeasure
            } catch (e: Exception) {
                DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
            }
        }
    }

    override fun updatedMeasure(request: DbMeasureRequest): DbMeasureResponse {
        return transaction {
            try {
                val measure = request.measure
                val measureUuid = UUID.fromString(measure.id.value)

                // Check if measure exists
                val exists = MeasuresTable.selectAll().where { MeasuresTable.id eq measureUuid }.count()
                if (exists == 0L) {
                    return@transaction DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }

                // Update measure
                MeasuresTable.update({ MeasuresTable.id eq measureUuid }) {
                    it[code] = measure.code
                    it[createdAt] = measure.createdAt
                }

                // Delete old translations and insert new ones
                MeasureTranslationsTable.deleteWhere { measureId eq measureUuid }
                request.translations.forEach { translation ->
                    MeasureTranslationsTable.insert {
                        it[measureId] = measureUuid
                        it[locale] = translation.locale
                        it[measureName] = translation.name
                        it[measureShortName] = translation.shortName
                    }
                }

                DbMeasureResponse(
                    result = BeMeasureWithTranslations(measure, request.translations),
                    isSuccess = true
                )
            } catch (e: Exception) {
                DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
            }
        }
    }

    override fun deletedMeasure(request: DbMeasureIdRequest): DbMeasureResponse {
        return transaction {
            try {
                val uuid = UUID.fromString(request.id.value)

                // Load measure before deleting
                val measureRow = MeasuresTable.selectAll().where { MeasuresTable.id eq uuid }.singleOrNull()
                if (measureRow == null) {
                    return@transaction DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }

                val measure = rowToBeMeasure(measureRow)
                val translations = loadTranslationsForMeasure(measure.id)

                // Delete translations first (FK constraint)
                MeasureTranslationsTable.deleteWhere { measureId eq uuid }

                // Delete measure
                val deleted = MeasuresTable.deleteWhere { id eq uuid }

                if (deleted > 0) {
                    DbMeasureResponse(
                        result = BeMeasureWithTranslations(measure, translations),
                        isSuccess = true
                    )
                } else {
                    DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
                }
            } catch (e: Exception) {
                DbMeasureResponse(result = BeMeasureWithTranslations.NONE, isSuccess = false)
            }
        }
    }

    override fun seedMeasures(requests: List<DbMeasureRequest>): DbMeasuresResponse {
        return transaction {
            try {
                val seededMeasures = requests.map { request ->
                    val measure = request.measure
                    val measureUuid = UUID.fromString(measure.id.value)

                    // Insert measure (or ignore if exists)
                    MeasuresTable.insert {
                        it[id] = measureUuid
                        it[code] = measure.code
                        it[createdAt] = measure.createdAt
                    }

                    // Insert translations
                    request.translations.forEach { translation ->
                        MeasureTranslationsTable.insert {
                            it[measureId] = measureUuid
                            it[locale] = translation.locale
                            it[measureName] = translation.name
                            it[measureShortName] = translation.shortName
                        }
                    }

                    BeMeasureWithTranslations(measure, request.translations)
                }

                DbMeasuresResponse(result = seededMeasures, isSuccess = true)
            } catch (e: Exception) {
                DbMeasuresResponse(result = emptyList(), isSuccess = false)
            }
        }
    }

    // Helper functions

    private fun loadAllMeasuresWithTranslations(): List<BeMeasureWithTranslations> {
        val measures = MeasuresTable.selectAll().map { rowToBeMeasure(it) }
        return measures.map { measure ->
            val translations = loadTranslationsForMeasure(measure.id)
            BeMeasureWithTranslations(measure, translations)
        }
    }

    private fun loadTranslationsForMeasure(measureId: BeId): List<BeMeasureTranslation> {
        val uuid = UUID.fromString(measureId.value)
        return MeasureTranslationsTable.selectAll()
            .where { MeasureTranslationsTable.measureId eq uuid }
            .map { rowToBeMeasureTranslation(it, measureId) }
    }

    private fun rowToBeMeasure(row: ResultRow): BeMeasure {
        return BeMeasure(
            id = BeId(row[MeasuresTable.id].toString()),
            code = row[MeasuresTable.code],
            createdAt = row[MeasuresTable.createdAt]
        )
    }

    private fun rowToBeMeasureTranslation(row: ResultRow, measureId: BeId): BeMeasureTranslation {
        return BeMeasureTranslation(
            id = measureId,
            locale = row[MeasureTranslationsTable.locale],
            name = row[MeasureTranslationsTable.measureName],
            shortName = row[MeasureTranslationsTable.measureShortName]
        )
    }
}
