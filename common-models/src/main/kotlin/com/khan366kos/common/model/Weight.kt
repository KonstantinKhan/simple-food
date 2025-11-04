package com.khan366kos.common.model

data class Weight(
    val value: Double,
    val measure: Measure
) {
    companion object {
        val NONE = Weight(
            value = 0.0,
            measure = Measure.g
        )
    }
}


