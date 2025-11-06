package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.BeProduct
import com.khan366kos.transport.model.Product as TransportProduct

fun BeProduct.toTransport(): TransportProduct = TransportProduct(
    productId = productId.asUUID(),
    productName = productName,
    productCalories = productCalories.toTransport(),
    productProteins = productProteins.toTransport(),
    productFats = productFats.toTransport(),
    productCarbohydrates = productCarbohydrates.toTransport(),
    weight = weight.toTransport(),
    author = author.toTransport(),
    categories = categories.toTransport()
)