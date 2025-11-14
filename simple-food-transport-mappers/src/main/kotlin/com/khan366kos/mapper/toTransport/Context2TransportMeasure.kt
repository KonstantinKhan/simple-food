package com.khan366kos.mapper.toTransport

import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasureWithTranslations
import com.khan366kos.transport.model.MeasureDetail
import com.khan366kos.transport.model.MeasureTranslation

fun BeMeasureWithTranslations.toMeasureTranslation(): MeasureDetail = MeasureDetail(
    id = measure.id.asUUID(),
    code = measure.code.value,
    translations = translations.map { it.toMeasureTranslation() }
)

fun BeMeasureTranslation.toMeasureTranslation(): MeasureTranslation = MeasureTranslation(
    locale = locale.value,
    measureName = name.value,
    measureShortName = shortName.value
)
