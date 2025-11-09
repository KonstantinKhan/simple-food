package com.khan366kos.measures.repository

import com.khan366kos.measures.model.BeMeasure
import com.khan366kos.measures.model.BeMeasureTranslation

data class DbMeasureRequest(
    val measure: BeMeasure,
    val translations: List<BeMeasureTranslation> = emptyList()
)
