package com.khan366kos.common.model.common

import com.khan366kos.common.model.measure.BeMeasureTranslation

data class BeWeight(
    val value: BeWeightValue,
    val measure: BeMeasureTranslation
) {
    companion object {
        val NONE = BeWeight(
            value = BeWeightValue.NONE,
            measure = BeMeasureTranslation.NONE
        )
    }
}