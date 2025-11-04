package com.khan366kos.mapper

import com.khan366kos.common.model.Calories as CommonCalories
import com.khan366kos.common.model.Carbohydrates as CommonCarbohydrates
import com.khan366kos.common.model.Fats as CommonFats
import com.khan366kos.common.model.Id as CommonId
import com.khan366kos.common.model.Measure as CommonMeasure
import com.khan366kos.common.model.Product as CommonProduct
import com.khan366kos.common.model.ProductType as CommonProductType
import com.khan366kos.common.model.Proteins as CommonProteins
import com.khan366kos.common.model.Title as CommonTitle
import com.khan366kos.transport.model.Product as TransportProduct
import java.util.UUID

internal fun Double?.orZero(): Double = this ?: 0.0

fun TransportProduct.toCommon(): CommonProduct = CommonProduct(
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
    type = CommonProductType(type ?: ""),
    categories = (categories ?: emptyList()).map { it.toCommonCategory() }
)

fun CommonProduct.toTransport(): TransportProduct = TransportProduct(
    id = UUID.fromString(id.value),
    title = title.value,
    calories = calories.value.toFloat(),
    proteins = proteins.value.toFloat(),
    fats = fats.value.toFloat(),
    carbohydrates = carbohydrates.value.toFloat(),
    weight = weight.toTransport(),
    author = author.toTransport(),
    type = type.value,
    categories = categories.map { it.value }
)


