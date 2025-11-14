package com.khan366kos.mapper.toContext

import com.khan366kos.common.model.user.BeAuthor
import com.khan366kos.common.model.user.BeAuthorName
import com.khan366kos.common.model.user.BeEmail
import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeLocale
import com.khan366kos.common.model.common.BeNutrientTitle
import com.khan366kos.common.model.common.BeNutrientShortTitle
import com.khan366kos.common.model.common.BeNutrientValue
import com.khan366kos.common.model.common.BeWeightValue
import com.khan366kos.common.model.measure.BeMeasureName
import com.khan366kos.common.model.measure.BeMeasureShortName
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.common.BeWeight
import com.khan366kos.common.model.common.BeCategories
import com.khan366kos.common.model.common.BeCategory
import com.khan366kos.common.model.common.BeCalories
import com.khan366kos.common.model.common.BeCarbohydrates
import com.khan366kos.common.model.common.BeFats
import com.khan366kos.common.model.common.BeProteins
import com.khan366kos.transport.model.Measure as TransportMeasure
import com.khan366kos.transport.model.NutritionalValue as TransportNutritionalValue
import com.khan366kos.transport.model.Weight as TransportWeight
import com.khan366kos.transport.model.Author as TransportAuthor

fun TransportAuthor.toContext(): BeAuthor = BeAuthor(
    authorId = BeId(id),
    name = BeAuthorName(name ?: ""),
    email = BeEmail(email ?: "")
)

fun TransportNutritionalValue.toContextCalories(): BeCalories = BeCalories(
    title = BeNutrientTitle(title),
    shortTitle = BeNutrientShortTitle(shortTitle),
    value = BeNutrientValue(nutritionalValue.toDouble()),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextProteins(): BeProteins = BeProteins(
    title = BeNutrientTitle(title),
    shortTitle = BeNutrientShortTitle(shortTitle),
    value = BeNutrientValue(nutritionalValue.toDouble()),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextFats(): BeFats = BeFats(
    title = BeNutrientTitle(title),
    shortTitle = BeNutrientShortTitle(shortTitle),
    value = BeNutrientValue(nutritionalValue.toDouble()),
    measure = measure.toContext()
)

fun TransportNutritionalValue.toContextCarbohydrates(): BeCarbohydrates = BeCarbohydrates(
    title = BeNutrientTitle(title),
    shortTitle = BeNutrientShortTitle(shortTitle),
    value = BeNutrientValue(nutritionalValue.toDouble()),
    measure = measure.toContext()
)

fun TransportMeasure.toContext(): BeMeasureTranslation = BeMeasureTranslation(
    id = BeId(id),
    locale = BeLocale(""),
    name = BeMeasureName(measureName),
    shortName = BeMeasureShortName(measureShortName)
)

fun TransportWeight.toContext(): BeWeight = BeWeight(
    value = BeWeightValue(weightValue.toDouble()),
    measure = measure.toContext()
)

fun List<String>?.toContextCategories(): BeCategories = this
    ?.takeIf { it.isNotEmpty() }
    ?.map { category -> BeCategory(category) }
    ?.let { BeCategories(it) }
    ?: BeCategories.NONE