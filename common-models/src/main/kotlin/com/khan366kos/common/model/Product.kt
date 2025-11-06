package com.khan366kos.common.model

data class Product(
    val id: Id,
    val title: Title,
    val calories: Calories,
    val proteins: Proteins,
    val fats: Fats,
    val carbohydrates: Carbohydrates,
    val weight: Weight,
    val author: Author,
    val categories: List<Category>
) {
    companion object {
        val NONE = Product(
            id = Id.NONE,
            title = Title.NONE,
            calories = Calories.NONE,
            proteins = Proteins.NONE,
            fats = Fats.NONE,
            carbohydrates = Carbohydrates.NONE,
            weight = Weight.NONE,
            author = Author.NONE,
            categories = emptyList()
        )
    }
}