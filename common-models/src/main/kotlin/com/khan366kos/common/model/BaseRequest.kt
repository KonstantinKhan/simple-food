package com.khan366kos.common.model

data class BaseRequest(
    val requestId: Id,
    val timestamp: String,
    val author: Author
) {
    companion object {
        val NONE = BaseRequest(
            requestId = Id.NONE,
            timestamp = "",
            author = Author.NONE
        )
    }
}


