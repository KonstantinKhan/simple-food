package com.khan366kos.common.model.common

@JvmInline
value class BeSearchString(val value: String) {
    companion object {
        val NONE = BeSearchString("")
    }
}