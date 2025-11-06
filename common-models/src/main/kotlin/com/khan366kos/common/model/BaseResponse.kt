package com.khan366kos.common.model

data class BaseResponse(
    val requestId: Id,
    val success: Boolean,
    val error: Error?
) {
    companion object {
        val NONE = BaseResponse(
            requestId = Id.NONE,
            success = false,
            error = null
        )
    }
}


