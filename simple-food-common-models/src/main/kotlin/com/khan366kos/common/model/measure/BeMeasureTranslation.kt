package com.khan366kos.common.model.measure

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeLocale

/**
 * Translation for a measure in a specific locale.
 *
 * @property id ID of the measure this translation belongs to
 * @property locale Locale code (e.g., "en", "ru", "fr")
 * @property name Full name of the measure in this locale
 * @property shortName Short abbreviation in this locale
 */
data class BeMeasureTranslation(
    val id: BeId = BeId.NONE,
    val locale: BeLocale = BeLocale.NONE,
    val name: BeMeasureName = BeMeasureName.NONE,
    val shortName: BeMeasureShortName = BeMeasureShortName.NONE
) {
    companion object {
        val NONE = BeMeasureTranslation()
    }
}