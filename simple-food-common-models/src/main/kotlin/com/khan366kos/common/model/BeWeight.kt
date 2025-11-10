package com.khan366kos.common.model

import com.khan366kos.common.model.measure.BeMeasureTranslation

data class BeWeight(
    val value: Double,
    val measure: BeMeasureTranslation
) {
    companion object {
        val NONE = BeWeight(
            value = 0.0,
            measure = BeMeasureTranslation.NONE
        )
    }
}


