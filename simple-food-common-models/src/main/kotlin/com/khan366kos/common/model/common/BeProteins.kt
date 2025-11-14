package com.khan366kos.common.model.common

import com.khan366kos.common.model.measure.BeMeasureTranslation

data class BeProteins(
    val title: BeNutrientTitle,
    val shortTitle: BeNutrientShortTitle,
    val value: BeNutrientValue,
    val measure: BeMeasureTranslation
) {
    companion object {
        val NONE = BeProteins(
            title = BeNutrientTitle.NONE,
            shortTitle = BeNutrientShortTitle.NONE,
            value = BeNutrientValue.NONE,
            measure = BeMeasureTranslation.NONE
        )
    }
}