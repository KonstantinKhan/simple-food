package com.khan366kos.common.model

import com.khan366kos.common.model.simple.BeCalories
import com.khan366kos.common.model.simple.BeCarbohydrates
import com.khan366kos.common.model.simple.BeFats
import com.khan366kos.common.model.simple.BeCategories
import com.khan366kos.common.model.simple.BeProteins

data class BeProduct(
    val productId: BeId,
    val productName: String,
    val productCalories: BeCalories,
    val productProteins: BeProteins,
    val productFats: BeFats,
    val productCarbohydrates: BeCarbohydrates,
    val weight: BeWeight,
    val author: BeAuthor,
    val categories: BeCategories
) {
    companion object {
        val NONE = BeProduct(
            productId = BeId.NONE,
            productName = "",
            productCalories = BeCalories.NONE,
            productProteins = BeProteins.NONE,
            productFats = BeFats.NONE,
            productCarbohydrates = BeCarbohydrates.NONE,
            weight = BeWeight.NONE,
            author = BeAuthor.NONE,
            categories = BeCategories.NONE
        )
    }
}