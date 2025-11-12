package com.khan366kos.common.model.user

import com.khan366kos.common.model.common.BeId

data class BeAuthor(
    val authorId: BeId,
    val name: String,
    val email: String
) {
    companion object {
        val NONE = BeAuthor(
            authorId = BeId.Companion.NONE,
            name = "",
            email = ""
        )
    }
}