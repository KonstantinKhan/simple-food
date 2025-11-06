package com.khan366kos.common.model

data class Measure(
    val measureName: String,
    val measureShortName: String
) {
    companion object {
        val NONE = Measure(
            measureName = "",
            measureShortName = ""
        )
    }
}


