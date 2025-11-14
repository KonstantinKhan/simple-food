package com.khan366kos.common.model.user

@JvmInline
value class BeEmail(val value: String) {
    companion object {
        val NONE = BeEmail("")
    }
}