package com.khan366kos.common.model.user

import com.khan366kos.common.model.common.BeId

data class BeAuthor(
    val authorId: BeId,
    val name: BeAuthorName,
    val email: BeEmail
) {
    companion object {
        val NONE = BeAuthor(
            authorId = BeId.NONE,
            name = BeAuthorName.NONE,
            email = BeEmail.NONE
        )
    }
}