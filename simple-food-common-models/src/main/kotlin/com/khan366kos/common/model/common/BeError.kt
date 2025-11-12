package com.khan366kos.common.model.common

data class BeError(
    val code: String,
    val message: String,
    val details: Map<String, Any?>
) {
    companion object {
        val NONE = BeError(
            code = "",
            message = "",
            details = emptyMap()
        )
    }
}