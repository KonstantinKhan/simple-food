package com.khan366kos.common.model.measure.repository

/**
 * Repository interface for measure operations.
 */
interface IRepoMeasure {
    /**
     * Get all measures with their translations.
     */
    fun measures(): DbMeasuresResponse

    /**
     * Get measures filtered by locale and/or search text.
     */
    fun foundMeasures(request: DbMeasureFilterRequest): DbMeasuresResponse

    /**
     * Get a single measure by ID.
     */
    fun measure(request: DbMeasureIdRequest): DbMeasureResponse

    /**
     * Get a single measure by code.
     */
    fun measureByCode(request: DbMeasureCodeRequest): DbMeasureResponse

    /**
     * Create a new measure with translations.
     */
    fun newMeasure(request: DbMeasureRequest): DbMeasureResponse

    /**
     * Update an existing measure with translations.
     */
    fun updatedMeasure(request: DbMeasureRequest): DbMeasureResponse

    /**
     * Delete a measure by ID.
     */
    fun deletedMeasure(request: DbMeasureIdRequest): DbMeasureResponse

    /**
     * Seed initial measures with translations.
     * Used for initialization.
     */
    fun seedMeasures(requests: List<DbMeasureRequest>): DbMeasuresResponse
}
