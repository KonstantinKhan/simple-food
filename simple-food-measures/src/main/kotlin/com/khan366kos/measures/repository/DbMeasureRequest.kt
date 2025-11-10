package com.khan366kos.measures.repository

import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasure

data class DbMeasureRequest(
    val measure: BeMeasure,
    val translations: List<BeMeasureTranslation> = emptyList()
)
