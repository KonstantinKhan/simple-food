package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.transport.model.Author as TransportAuthor
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.transport.model.Weight as TransportWeight

fun BeMeasureTranslation.toTransport(): TransportMeasure = TransportMeasure(
    id = id.asUUID(),
    code = "",
    measureName = name.value,
    measureShortName = shortName.value
)

fun BeWeight.toMeasureTranslation(): TransportWeight = TransportWeight(
    weightValue = value.value.toFloat(),
    measure = measure.toTransport()
)

fun BeCategories.toMeasureTranslation(): List<String> = value.map { it.value }

fun BeAuthor.toMeasureTranslation(): TransportAuthor = TransportAuthor(
    id = authorId.asUUID(),
    name = name.value,
    email = email.value
)

fun BeCalories.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title.value,
    shortTitle = shortTitle.value,
    nutritionalValue = value.value.toFloat(),
    measure = measure.toTransport()
)

fun BeProteins.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title.value,
    shortTitle = shortTitle.value,
    nutritionalValue = value.value.toFloat(),
    measure = measure.toTransport()
)

fun BeFats.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title.value,
    shortTitle = shortTitle.value,
    nutritionalValue = value.value.toFloat(),
    measure = measure.toTransport()
)

fun BeCarbohydrates.toMeasureTranslation(): TransportNutritionalValue = TransportNutritionalValue(
    title = title.value,
    shortTitle = shortTitle.value,
    nutritionalValue = value.value.toFloat(),
    measure = measure.toTransport()
)