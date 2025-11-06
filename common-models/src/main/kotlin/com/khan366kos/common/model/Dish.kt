package com.khan366kos.common.model

import com.khan366kos.common.model.simple.Calories
import com.khan366kos.common.model.simple.Carbohydrates
import com.khan366kos.common.model.simple.Fats
import com.khan366kos.common.model.simple.Categories

data class Dish(
    val id: Id,
    val title: String,
    val calories: Calories,
    val proteins: Proteins,
    val fats: Fats,
    val carbohydrates: Carbohydrates,
    val weight: Weight,
    val author: Author,
    val categories: Categories,
    val recipes: List<String>,
    val products: List<ProductPortion>
) {
    companion object {
        val NONE = Dish(
            id = Id.NONE,
            title = "",
            calories = Calories.NONE,
            proteins = Proteins.NONE,
            fats = Fats.NONE,
            carbohydrates = Carbohydrates.NONE,
            weight = Weight.NONE,
            author = Author.NONE,
            categories = Categories.NONE,
            recipes = emptyList(),
            products = emptyList()
        )
    }
}


