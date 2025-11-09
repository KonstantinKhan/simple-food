package com.khan366kos.common.model

data class BeProductPortion(
    val productId: BeId,
    val weight: BeWeight
) {
    companion object {
        val NONE = BeProductPortion(
            productId = BeId.NONE,
            weight = BeWeight.NONE
        )
    }
}


