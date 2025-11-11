package com.khan366kos.common.model.measure

import com.khan366kos.common.model.BeId

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
    val locale: String = "",
    val code: String = "",
    val name: String = "",
    val shortName: String = ""
) {
    companion object {
        val NONE = BeMeasureTranslation()
    }
}