package com.khan366kos.common.model.simple

import com.khan366kos.common.model.Measure

data class Carbohydrates(
    val name: String,
    val shortName: String,
    val measure: Measure,
    val value: Double
) {
    companion object {
        val NONE = Carbohydrates(
            name = "",
            shortName = "",
            measure = Measure.g,
            value = 0.0
        )
    }
}


