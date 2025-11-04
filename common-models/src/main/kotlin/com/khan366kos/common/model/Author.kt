package com.khan366kos.common.model

data class Author(
    val id: Id,
    val name: String,
    val email: String
) {
    companion object {
        val NONE = Author(
            id = Id.NONE,
            name = "",
            email = ""
        )
    }
}


