package com.khan366kos.common.model.product

import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.user.BeAuthor

data class BeProduct(
    val productId: BeId,
    val productName: BeProductName,
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
            productName = BeProductName.NONE,
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