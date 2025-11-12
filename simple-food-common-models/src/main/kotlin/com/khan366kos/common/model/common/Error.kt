package com.khan366kos.common.model.common

data class Error(
    val code: String,
    val message: String,
    val details: Map<String, Any?>
) {
    companion object {
        val NONE = Error(
            code = "",
            message = "",
            details = emptyMap()
        )
    }
}