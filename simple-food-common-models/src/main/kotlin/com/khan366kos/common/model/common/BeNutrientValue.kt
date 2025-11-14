package com.khan366kos.common.model.common

@JvmInline
value class BeNutrientValue(val value: Double) {
    companion object {
        val NONE = BeNutrientValue(0.0)
    }
}