package com.khan366kos.mapper

import com.khan366kos.common.model.Calories as CommonCalories
import com.khan366kos.common.model.Carbohydrates as CommonCarbohydrates
import com.khan366kos.common.model.Dish as CommonDish
import com.khan366kos.common.model.Fats as CommonFats
import com.khan366kos.common.model.Id as CommonId
import com.khan366kos.common.model.Measure as CommonMeasure
import com.khan366kos.common.model.ProductPortion as CommonProductPortion
import com.khan366kos.common.model.Proteins as CommonProteins
import com.khan366kos.common.model.RecipeStep as CommonRecipeStep
import com.khan366kos.common.model.Title as CommonTitle
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion
import java.util.UUID

private fun TransportProductPortion.toCommon(): CommonProductPortion = CommonProductPortion(
    productId = productId.toCommonId(),
    weight = weight.toCommon()
)

private fun CommonProductPortion.toTransport(): TransportProductPortion = TransportProductPortion(
    productId = UUID.fromString(productId.value),
    weight = weight.toTransport()
)

fun TransportDish.toCommon(): CommonDish = CommonDish(
    id = id.toCommonId(),
    title = (title).let { CommonTitle(it) },
    calories = CommonCalories(
        name = "Calories",
        shortName = "kcal",
        measure = CommonMeasure.g,
        value = calories.toDouble()
    ),
    proteins = CommonProteins(
        name = "Proteins",
        shortName = "P",
        measure = CommonMeasure.g,
        value = proteins.toDouble()
    ),
    fats = CommonFats(
        name = "Fats",
        shortName = "F",
        measure = CommonMeasure.g,
        value = fats.toDouble()
    ),
    carbohydrates = CommonCarbohydrates(
        name = "Carbohydrates",
        shortName = "C",
        measure = CommonMeasure.g,
        value = carbohydrates.toDouble()
    ),
    weight = weight.toCommon(),
    author = author.toCommon(),
    categories = (categories ?: emptyList()).map { it.toCommonCategory() },
    recipes = (recipes ?: emptyList()).map { CommonRecipeStep(it ?: "") },
    products = (products ?: emptyList()).map { it.toCommon() }
)

fun CommonDish.toTransport(): TransportDish = TransportDish(
    id = UUID.fromString(id.value),
    title = title.value,
    calories = calories.value.toFloat(),
    proteins = proteins.value.toFloat(),
    fats = fats.value.toFloat(),
    carbohydrates = carbohydrates.value.toFloat(),
    weight = weight.toTransport(),
    author = author.toTransport(),
    categories = categories.map { it.value },
    recipes = recipes.map { it.value },
    products = products.map { it.toTransport() }
)


