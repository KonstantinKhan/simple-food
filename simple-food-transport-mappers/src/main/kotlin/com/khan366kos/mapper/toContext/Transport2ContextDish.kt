package com.khan366kos.mapper.toContext

import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.dish.BeDish
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.dish.BeProductPortion
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion

private fun TransportProductPortion.toContext(): BeProductPortion = BeProductPortion(
    productId = BeId(productId),
    weight = weight.toContext()
)

fun TransportDish.toContext(): BeDish = BeDish(
    id = BeId(id),
    title = title,
    calories = calories.toContextCalories(),
    proteins = proteins.toContextProteins(),
    fats = fats.toContextFats(),
    carbohydrates = carbohydrates.toContextCarbohydrates(),
    weight = weight.toContext(),
    author = author?.toContext() ?: BeAuthor.NONE,
    categories = categories.toContextCategories(),
    recipes = recipes ?: emptyList(),
    products = (products ?: emptyList()).map { it.toContext() }
)


