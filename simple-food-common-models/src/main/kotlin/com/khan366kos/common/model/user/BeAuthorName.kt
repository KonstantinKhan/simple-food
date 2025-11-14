package com.khan366kos.common.model.user

@JvmInline
value class BeAuthorName(val value: String) {
    companion object {
        val NONE = BeAuthorName("")
    }
}