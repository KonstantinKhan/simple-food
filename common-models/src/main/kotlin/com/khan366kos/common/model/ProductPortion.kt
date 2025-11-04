package com.khan366kos.common.model

data class ProductPortion(
    val productId: Id,
    val weight: Weight
) {
    companion object {
        val NONE = ProductPortion(
            productId = Id.NONE,
            weight = Weight.NONE
        )
    }
}


