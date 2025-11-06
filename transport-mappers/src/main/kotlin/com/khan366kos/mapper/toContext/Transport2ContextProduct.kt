package com.khan366kos.mapper.toContext

import com.khan366kos.common.model.BeAuthor
import com.khan366kos.transport.model.Product as TransportProduct
import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeProduct
import com.khan366kos.common.model.simple.BeCategories
import com.khan366kos.common.model.simple.BeCategory

fun TransportProduct.toContext(): BeProduct = BeProduct(
    productId = BeId(productId),
    productName = productName,
    productCalories = productCalories.toContextCalories(),
    productProteins = productProteins.toContextProteins(),
    productFats = productFats.toContextFats(),
    productCarbohydrates = productCarbohydrates.toContextCarbohydrates(),
    weight = weight.toContext(),
    author = author?.toContext() ?: BeAuthor.NONE,
    categories = categories
        ?.takeIf { it.isNotEmpty() }
        ?.map { category -> BeCategory(category) }
        ?.let { BeCategories(it) }
        ?: BeCategories.NONE
)

