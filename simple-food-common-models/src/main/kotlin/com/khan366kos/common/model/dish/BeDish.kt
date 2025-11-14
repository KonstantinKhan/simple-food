package com.khan366kos.common.model.dish

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.dish.BeProductPortion
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.common.BeRecipeStep
import com.khan366kos.common.model.user.BeAuthor

data class BeDish(
    val id: BeId,
    val title: BeDishTitle,
    val calories: BeCalories,
    val proteins: BeProteins,
    val fats: BeFats,
    val carbohydrates: BeCarbohydrates,
    val weight: BeWeight,
    val author: BeAuthor,
    val categories: BeCategories,
    val recipes: List<BeRecipeStep>,
    val products: List<BeProductPortion>
) {
    companion object {
        val NONE = BeDish(
            id = BeId.NONE,
            title = BeDishTitle.NONE,
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