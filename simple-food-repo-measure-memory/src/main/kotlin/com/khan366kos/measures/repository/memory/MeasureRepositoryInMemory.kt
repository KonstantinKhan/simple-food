package com.khan366kos.measures.repository.memory

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasureWithTranslations
import com.khan366kos.common.model.measure.repository.*
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class MeasureRepositoryInMemory : IRepoMeasure {
    private val measures = ConcurrentHashMap<BeId, BeMeasure>()
    private val translations = ConcurrentHashMap<BeId, MutableList<BeMeasureTranslation>>()

    override fun measures(): DbMeasuresResponse {
        val allMeasures = measures.values.map { measure ->
            BeMeasureWithTranslations(
                measure = measure,
                translations = translations[measure.id] ?: emptyList()
            )
        }
        return DbMeasuresResponse(isSuccess = true, result = allMeasures)
    }

    override fun foundMeasures(request: DbMeasureFilterRequest): DbMeasuresResponse {
        val allMeasures = measures.values.map { measure ->
            val measureTranslations = translations[measure.id] ?: emptyList()
            BeMeasureWithTranslations(measure = measure, translations = measureTranslations)
        }

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

        return DbMeasuresResponse(isSuccess = true, result = filtered)
    }

    override fun measure(request: DbMeasureIdRequest): DbMeasureResponse {
        val measure = measures[request.id]
        return if (measure != null) {
            val measureTranslations = translations[measure.id] ?: emptyList()
            DbMeasureResponse(
                isSuccess = true,
                result = BeMeasureWithTranslations(measure, measureTranslations)
            )
        } else {
            DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
        }
    }

    override fun measureByCode(request: DbMeasureCodeRequest): DbMeasureResponse {
        val measure = measures.values.find { it.code == request.code }
        return if (measure != null) {
            val measureTranslations = translations[measure.id] ?: emptyList()
            DbMeasureResponse(
                isSuccess = true,
                result = BeMeasureWithTranslations(measure, measureTranslations)
            )
        } else {
            DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
        }
    }

    override fun newMeasure(request: DbMeasureRequest): DbMeasureResponse {
        val measure = request.measure
        val generatedId = BeId(UUID.randomUUID())
        val measureWithId = measure.copy(id = generatedId)

        // Check if code already exists
        if (measures.values.any { it.code == measure.code }) {
            return DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
        }

        measures[generatedId] = measureWithId
        if (request.translations.isNotEmpty()) {
            translations[generatedId] = request.translations.map { it.copy(id = generatedId) }.toMutableList()
        }

        return DbMeasureResponse(
            isSuccess = true,
            result = BeMeasureWithTranslations(measureWithId, request.translations)
        )
    }

    override fun updatedMeasure(request: DbMeasureRequest): DbMeasureResponse {
        val measure = request.measure
        return if (measures.containsKey(measure.id)) {
            measures[measure.id] = measure
            if (request.translations.isNotEmpty()) {
                translations[measure.id] = request.translations.toMutableList()
            }
            DbMeasureResponse(
                isSuccess = true,
                result = BeMeasureWithTranslations(measure, request.translations)
            )
        } else {
            DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
        }
    }

    override fun deletedMeasure(request: DbMeasureIdRequest): DbMeasureResponse {
        val measure = measures.remove(request.id)
        val measureTranslations = translations.remove(request.id) ?: emptyList()

        return if (measure != null) {
            DbMeasureResponse(
                isSuccess = true,
                result = BeMeasureWithTranslations(measure, measureTranslations)
            )
        } else {
            DbMeasureResponse(isSuccess = false, result = BeMeasureWithTranslations.NONE)
        }
    }

    override fun seedMeasures(requests: List<DbMeasureRequest>): DbMeasuresResponse {
        val seededMeasures = requests.map { request ->
            val measure = request.measure
            measures[measure.id] = measure
            if (request.translations.isNotEmpty()) {
                translations[measure.id] = request.translations.toMutableList()
            }
            BeMeasureWithTranslations(measure, request.translations)
        }

        return DbMeasuresResponse(isSuccess = true, result = seededMeasures)
    }
}
