package com.khan366kos.common.model

data class Dish(
    val id: Id,
    val title: Title,
    val calories: Calories,
    val proteins: Proteins,
    val fats: Fats,
    val carbohydrates: Carbohydrates,
    val weight: Weight,
    val author: Author,
    val type: DishType,
    val categories: List<Category>,
    val recipes: List<RecipeStep>,
    val products: List<ProductPortion>
) {
    companion object {
        val NONE = Dish(
            id = Id.NONE,
            title = Title.NONE,
            calories = Calories.NONE,
            proteins = Proteins.NONE,
            fats = Fats.NONE,
            carbohydrates = Carbohydrates.NONE,
            weight = Weight.NONE,
            author = Author.NONE,
            type = DishType.NONE,
            categories = emptyList(),
            recipes = emptyList(),
            products = emptyList()
        )
    }
}


