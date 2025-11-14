package com.khan366kos.common.model.common

data class BeError(
    val code: BeErrorCode,
    val message: BeErrorMessage,
    val details: Map<String, Any?>
) {
    companion object {
        val NONE = BeError(
            code = BeErrorCode.NONE,
            message = BeErrorMessage.NONE,
            details = emptyMap()
        )
    }
}