package com.khan366kos.common.model

data class BeMeasure(
    val id: BeId = BeId.NONE,
    val code: String = "",
    val measureName: String = "",
    val measureShortName: String = ""
) {
    companion object {
        val NONE = BeMeasure()
    }
}


