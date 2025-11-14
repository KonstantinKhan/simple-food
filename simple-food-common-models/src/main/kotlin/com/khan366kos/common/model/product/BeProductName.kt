package com.khan366kos.common.model.product

@JvmInline
value class BeProductName(val value: String) {
    companion object {
        val NONE = BeProductName("")
    }
}