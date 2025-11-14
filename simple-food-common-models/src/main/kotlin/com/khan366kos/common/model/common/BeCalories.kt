package com.khan366kos.common.model.common

import com.khan366kos.common.model.measure.BeMeasureTranslation

data class BeCalories(
    val title: BeNutrientTitle,
    val shortTitle: BeNutrientShortTitle,
    val value: BeNutrientValue,
    val measure: BeMeasureTranslation
) {
    companion object {
        val NONE = BeCalories(
            title = BeNutrientTitle.NONE,
            shortTitle = BeNutrientShortTitle.NONE,
            value = BeNutrientValue.NONE,
            measure = BeMeasureTranslation.NONE
        )
    }
}