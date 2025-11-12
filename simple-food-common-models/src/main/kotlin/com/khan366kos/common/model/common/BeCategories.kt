package com.khan366kos.common.model.common

import com.khan366kos.common.model.common.BeCategory

@JvmInline
value class BeCategories(val value: List<BeCategory>) {
    companion object {
        val NONE = BeCategories(emptyList())
    }
}