package com.khan366kos.common.model.simple

@JvmInline
value class ProductType(val value: String) {
    companion object {
        val NONE = ProductType("")
    }
}


