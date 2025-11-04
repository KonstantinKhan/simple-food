package com.khan366kos.common.model

@JvmInline
value class Id(val value: String) {
    companion object {
        val NONE = Id("")
    }
}


