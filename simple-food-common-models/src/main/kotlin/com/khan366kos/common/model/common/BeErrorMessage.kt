package com.khan366kos.common.model.common

@JvmInline
value class BeErrorMessage(val value: String) {
    companion object {
        val NONE = BeErrorMessage("")
    }
}