package com.khan366kos.common.model

data class BeWeight(
    val value: Double,
    val measure: BeMeasure
) {
    companion object {
        val NONE = BeWeight(
            value = 0.0,
            measure = BeMeasure.NONE
        )
    }
}


