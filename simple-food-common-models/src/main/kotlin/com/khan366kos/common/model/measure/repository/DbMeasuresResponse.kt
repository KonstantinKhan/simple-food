package com.khan366kos.common.model.measure.repository

import com.khan366kos.common.interfaces.IDbResponse
import com.khan366kos.common.model.measure.BeMeasureWithTranslations

data class DbMeasuresResponse(
    override val isSuccess: Boolean,
    override val result: List<BeMeasureWithTranslations>
) : IDbResponse<List<BeMeasureWithTranslations>>
