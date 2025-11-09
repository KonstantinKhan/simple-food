package com.khan366kos.measures.repository

import com.khan366kos.common.repository.IDbResponse
import com.khan366kos.measures.model.BeMeasureWithTranslations

data class DbMeasureResponse(
    override val isSuccess: Boolean,
    override val result: BeMeasureWithTranslations
) : IDbResponse<BeMeasureWithTranslations>
