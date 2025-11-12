package com.khan366kos.common.model.dish

import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeId

data class BeProductPortion(
    val productId: BeId,
    val weight: BeWeight
) {
    companion object {
        val NONE = BeProductPortion(
            productId = BeId.Companion.NONE,
            weight = BeWeight.Companion.NONE
        )
    }
}