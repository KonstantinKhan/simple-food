package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.BeAuthor
import com.khan366kos.common.model.BeMeasure
import com.khan366kos.common.model.BeWeight
import com.khan366kos.common.model.simple.BeCalories
import com.khan366kos.common.model.simple.BeCarbohydrates
import com.khan366kos.common.model.simple.BeCategories
import com.khan366kos.common.model.simple.BeFats
import com.khan366kos.common.model.simple.BeProteins
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.transport.model.Weight as TransportWeight

fun BeMeasure.toTransport(): TransportMeasure = TransportMeasure(
    id = id.asUUID(),
    code = code,
    measureName = measureName,
    measureShortName = measureShortName
)

fun BeWeight.toTransport(): TransportWeight = TransportWeight(
    weightValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeCategories.toTransport(): List<String> = value.map { it.value }

fun BeAuthor.toTransport(): TransportAuthor = TransportAuthor(
    id = authorId.asUUID(),
    name = name,
    email = email
)

fun BeCalories.toTransport(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeProteins.toTransport(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeFats.toTransport(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeCarbohydrates.toTransport(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)