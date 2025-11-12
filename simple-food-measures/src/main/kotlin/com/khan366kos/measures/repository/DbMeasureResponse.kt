package com.khan366kos.measures.repository

import com.khan366kos.common.interfaces.IDbResponse
import com.khan366kos.common.model.measure.BeMeasureWithTranslations

data class DbMeasureResponse(
    override val isSuccess: Boolean,
    override val result: BeMeasureWithTranslations
) : IDbResponse<BeMeasureWithTranslations>
