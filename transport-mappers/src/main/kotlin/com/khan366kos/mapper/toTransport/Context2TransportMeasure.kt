package com.khan366kos.mapper.toTransport

import com.khan366kos.measures.model.BeMeasureWithTranslations
import com.khan366kos.measures.model.BeMeasureTranslation
import com.khan366kos.transport.model.MeasureDetail
import com.khan366kos.transport.model.MeasureTranslation

fun BeMeasureWithTranslations.toTransport(): MeasureDetail = MeasureDetail(
    id = measure.id.asUUID(),
    code = measure.code,
    translations = translations.map { it.toTransport() }
)

fun BeMeasureTranslation.toTransport(): MeasureTranslation = MeasureTranslation(
    locale = locale,
    measureName = measureName,
    measureShortName = measureShortName
)
