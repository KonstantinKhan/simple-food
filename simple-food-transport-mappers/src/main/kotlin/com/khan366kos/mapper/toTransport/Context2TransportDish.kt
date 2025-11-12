package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.dish.BeDish
import com.khan366kos.common.model.dish.BeProductPortion
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion

fun BeProductPortion.toMeasureTranslation(): TransportProductPortion = TransportProductPortion(
	productId = productId.asUUID(),
	weight = weight.toMeasureTranslation()
)

fun BeDish.toMeasureTranslation(): TransportDish = TransportDish(
	id = id.asUUID(),
	title = title,
	calories = calories.toMeasureTranslation(),
	proteins = proteins.toMeasureTranslation(),
	fats = fats.toMeasureTranslation(),
	carbohydrates = carbohydrates.toMeasureTranslation(),
	weight = weight.toMeasureTranslation(),
	author = author.toMeasureTranslation(),
	categories = categories.toMeasureTranslation(),
	recipes = recipes,
	products = products.map { it.toMeasureTranslation() }
)


