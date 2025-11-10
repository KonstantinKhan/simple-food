package com.khan366kos.mapper.toContext

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.BeMeasureWithTranslations
import com.khan366kos.transport.model.MeasureDetail
import com.khan366kos.transport.model.MeasureTranslation
import java.time.Instant

fun MeasureDetail.toContext(): BeMeasureWithTranslations = BeMeasureWithTranslations(
    measure = BeMeasure(
        id = BeId(id),
        code = code,
        createdAt = Instant.now()
    ),
    translations = translations.map { it.toContext(BeId(id)) }
)

fun MeasureTranslation.toContext(measureId: BeId): BeMeasureTranslation = BeMeasureTranslation(
    id = measureId,
    locale = locale,
    name = measureName,
    shortName = measureShortName
)
