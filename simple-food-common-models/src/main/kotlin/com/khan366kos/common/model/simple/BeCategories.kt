package com.khan366kos.common.model.simple

@JvmInline
value class BeCategories(val value: List<BeCategory>) {
    companion object {
        val NONE = BeCategories(emptyList())
    }
}


