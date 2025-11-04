package com.khan366kos.common.model

import com.khan366kos.common.model.Measure

data class Calories(
    val name: String,
    val shortName: String,
    val measure: Measure,
    val value: Double
) {
    companion object {
        val NONE = Calories(
            name = "",
            shortName = "",
            measure = Measure.g,
            value = 0.0
        )
    }
}


