package com.khan366kos.common.model.common

@JvmInline
value class BeErrorCode(val value: String) {
    companion object {
        val NONE = BeErrorCode("")
    }
}