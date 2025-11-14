package com.khan366kos.common.model.common

@JvmInline
value class BeWeightValue(val value: Double) {
    companion object {
        val NONE = BeWeightValue(0.0)
    }
}