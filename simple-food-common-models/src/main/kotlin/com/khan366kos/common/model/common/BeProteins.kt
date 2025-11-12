package com.khan366kos.common.model.common

import com.khan366kos.common.model.measure.BeMeasureTranslation

data class BeProteins(
    val title: String,
    val shortTitle: String,
    val value: Double,
    val measure: BeMeasureTranslation
) {
    companion object {
        val NONE = BeProteins(
            title = "",
            shortTitle = "",
            value = 0.0,
            measure = BeMeasureTranslation.Companion.NONE
        )
    }
}