package com.khan366kos.common.model.measure

@JvmInline
value class BeMeasureName(val value: String) {
    companion object {
        val NONE = BeMeasureName("")
    }
}