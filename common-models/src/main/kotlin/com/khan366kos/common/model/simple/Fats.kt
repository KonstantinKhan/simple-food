package com.khan366kos.common.model.simple

import com.khan366kos.common.model.Measure

data class Fats(
    val name: String,
    val shortName: String,
    val measure: Measure,
    val value: Double
) {
    companion object {
        val NONE = Fats(
            name = "",
            shortName = "",
            measure = Measure.g,
            value = 0.0
        )
    }
}


