package com.khan366kos.common.model

data class BeAuthor(
    val authorId: BeId,
    val name: String,
    val email: String
) {
    companion object {
        val NONE = BeAuthor(
            authorId = BeId.NONE,
            name = "",
            email = ""
        )
    }
}


