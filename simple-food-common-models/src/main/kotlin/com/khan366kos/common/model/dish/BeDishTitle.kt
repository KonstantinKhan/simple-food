package com.khan366kos.common.model.dish

@JvmInline
value class BeDishTitle(val value: String) {
    companion object {
        val NONE = BeDishTitle("")
    }
}