package com.khan366kos.common.model

import com.khan366kos.common.model.simple.Calories
import com.khan366kos.common.model.simple.Carbohydrates
import com.khan366kos.common.model.simple.Fats
import com.khan366kos.common.model.simple.Categories
import com.khan366kos.common.model.simple.Proteins

data class Product(
    val productId: Id,
    val productName: String,
    val productCalories: Calories,
    val productProteins: Proteins,
    val productFats: Fats,
    val productCarbohydrates: Carbohydrates,
    val weight: Weight,
    val author: Author,
    val categories: Categories
) {
    companion object {
        val NONE = Product(
            productId = Id.NONE,
            productName = "",
            productCalories = Calories.NONE,
            productProteins = Proteins.NONE,
            productFats = Fats.NONE,
            productCarbohydrates = Carbohydrates.NONE,
            weight = Weight.NONE,
            author = Author.NONE,
            categories = Categories.NONE
        )
    }
}