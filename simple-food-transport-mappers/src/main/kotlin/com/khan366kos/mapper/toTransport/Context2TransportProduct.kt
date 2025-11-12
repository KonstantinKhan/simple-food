package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.product.BeProduct
import com.khan366kos.transport.model.Product as TransportProduct

fun BeProduct.toMeasureTranslation(): TransportProduct = TransportProduct(
    productId = productId.asUUID(),
    productName = productName,
    productCalories = productCalories.toMeasureTranslation(),
    productProteins = productProteins.toMeasureTranslation(),
    productFats = productFats.toMeasureTranslation(),
    productCarbohydrates = productCarbohydrates.toMeasureTranslation(),
    weight = weight.toMeasureTranslation(),
    author = author.toMeasureTranslation(),
    categories = categories.toMeasureTranslation()
)