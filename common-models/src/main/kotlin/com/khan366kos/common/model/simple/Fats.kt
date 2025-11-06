package com.khan366kos.common.model.simple

import com.khan366kos.common.model.Measure

data class Fats(
    val title: String,
    val shortTitle: String,
    val measure: Measure
) {
    companion object {
        val NONE = Fats(
            title = "",
            shortTitle = "",
            measure = Measure.NONE
        )
    }
}


