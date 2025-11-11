package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.BeAuthor
import com.khan366kos.common.model.measure.BeMeasureTranslation
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

fun BeMeasureTranslation.toTransport(): TransportMeasure = TransportMeasure(
    id = id.asUUID(),
    code = "",
    measureName = name,
    measureShortName = shortName
)

fun BeWeight.toMeasureTranslation(): TransportWeight = TransportWeight(
    weightValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeCategories.toMeasureTranslation(): List<String> = value.map { it.value }

fun BeAuthor.toMeasureTranslation(): TransportAuthor = TransportAuthor(
    id = authorId.asUUID(),
    name = name,
    email = email
)

fun BeCalories.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeProteins.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeFats.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)

fun BeCarbohydrates.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title,
    shortTitle = shortTitle,
    nutritionalValue = value.toFloat(),
    measure = measure.toTransport()
)