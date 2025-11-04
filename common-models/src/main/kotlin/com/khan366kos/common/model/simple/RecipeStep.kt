package com.khan366kos.common.model

@JvmInline
value class RecipeStep(val value: String) {
    companion object {
        val NONE = RecipeStep("")
    }
}


