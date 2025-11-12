package com.khan366kos.common.model.dish

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.dish.BeProductPortion
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.common.model.user.BeAuthor

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
            id = BeId.Companion.NONE,
            title = "",
            calories = BeCalories.Companion.NONE,
            proteins = BeProteins.Companion.NONE,
            fats = BeFats.Companion.NONE,
            carbohydrates = BeCarbohydrates.Companion.NONE,
            weight = BeWeight.Companion.NONE,
            author = BeAuthor.Companion.NONE,
            categories = BeCategories.Companion.NONE,
            recipes = emptyList(),
            products = emptyList()
        )
    }
}