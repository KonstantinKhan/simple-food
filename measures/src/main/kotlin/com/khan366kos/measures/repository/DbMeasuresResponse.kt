package com.khan366kos.measures.repository

import com.khan366kos.common.repository.IDbResponse
import com.khan366kos.common.model.measure.BeMeasureWithTranslations

data class DbMeasuresResponse(
    override val isSuccess: Boolean,
    override val result: List<BeMeasureWithTranslations>
) : IDbResponse<List<BeMeasureWithTranslations>>
