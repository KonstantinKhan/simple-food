package com.khan366kos.mapper.toContext

import com.khan366kos.common.model.BeAuthor
import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeMeasure
import com.khan366kos.common.model.BeWeight
import com.khan366kos.common.model.simple.BeCategories
import com.khan366kos.common.model.simple.BeCategory
import com.khan366kos.common.model.simple.BeCalories
import com.khan366kos.common.model.simple.BeCarbohydrates
import com.khan366kos.common.model.simple.BeFats
import com.khan366kos.common.model.simple.BeProteins
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor

fun TransportAuthor.toContext(): BeAuthor = BeAuthor(
    authorId = BeId(id),
    name = name ?: "",
    email = email ?: ""
)

fun TransportNutritionalValue.toContextCalories(): BeCalories = BeCalories(
    title = title,
    shortTitle = shortTitle,
    value = nutritionalValue.toDouble(),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextProteins(): BeProteins = BeProteins(
    title = title,
    shortTitle = shortTitle,
    value = nutritionalValue.toDouble(),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextFats(): BeFats = BeFats(
    title = title,
    shortTitle = shortTitle,
    value = nutritionalValue.toDouble(),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextCarbohydrates(): BeCarbohydrates = BeCarbohydrates(
    title = title,
    shortTitle = shortTitle,
    value = nutritionalValue.toDouble(),
    measure = measure.toContext()
)

fun TransportMeasure.toContext(): BeMeasure = BeMeasure(
    measureName = measureName,
    measureShortName = measureShortName
)

fun TransportWeight.toContext(): BeWeight = BeWeight(
    value = weightValue.toDouble(),
    measure = measure.toContext()
)

fun List<String>?.toContextCategories(): BeCategories = this
    ?.takeIf { it.isNotEmpty() }
    ?.map { category -> BeCategory(category) }
    ?.let { BeCategories(it) }
    ?: BeCategories.NONE