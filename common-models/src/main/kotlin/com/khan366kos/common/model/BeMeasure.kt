package com.khan366kos.common.model

data class BeMeasure(
    val measureName: String,
    val measureShortName: String
) {
    companion object {
        val NONE = BeMeasure(
            measureName = "",
            measureShortName = ""
        )
    }
}


