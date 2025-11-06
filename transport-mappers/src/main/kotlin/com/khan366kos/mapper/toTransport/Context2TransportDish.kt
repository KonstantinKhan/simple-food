package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.BeDish
import com.khan366kos.common.model.BeProductPortion
import com.khan366kos.transport.model.Dish as TransportDish
import com.khan366kos.transport.model.ProductPortion as TransportProductPortion

fun BeProductPortion.toTransport(): TransportProductPortion = TransportProductPortion(
	productId = productId.asUUID(),
	weight = weight.toTransport()
)

fun BeDish.toTransport(): TransportDish = TransportDish(
	id = id.asUUID(),
	title = title,
	calories = calories.toTransport(),
	proteins = proteins.toTransport(),
	fats = fats.toTransport(),
	carbohydrates = carbohydrates.toTransport(),
	weight = weight.toTransport(),
	author = author.toTransport(),
	categories = categories.toTransport(),
	recipes = recipes,
	products = products.map { it.toTransport() }
)


