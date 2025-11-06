package com.khan366kos.common.model.simple

@JvmInline
value class Categories(val value: List<Category>) {
    companion object {
        val NONE = Categories(emptyList())
    }
}


