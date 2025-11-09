package com.khan366kos.common.model

import com.khan366kos.common.model.simple.BeCalories
import com.khan366kos.common.model.simple.BeCarbohydrates
import com.khan366kos.common.model.simple.BeFats
import com.khan366kos.common.model.simple.BeCategories
import com.khan366kos.common.model.simple.BeProteins

data class BeDish(
    val id: BeId,
    val title: String,
    val calories: BeCalories,
    val proteins: BeProteins,
    val fats: BeFats,
    val carbohydrates: BeCarbohydrates,
    val weight: BeWeight,
    val author: BeAuthor,
    val categories: BeCategories,
    val recipes: List<String>,
    val products: List<BeProductPortion>
) {
    companion object {
        val NONE = BeDish(
            id = BeId.NONE,
            title = "",
            calories = BeCalories.NONE,
            proteins = BeProteins.NONE,
            fats = BeFats.NONE,
            carbohydrates = BeCarbohydrates.NONE,
            weight = BeWeight.NONE,
            author = BeAuthor.NONE,
            categories = BeCategories.NONE,
            recipes = emptyList(),
            products = emptyList()
        )
    }
}


