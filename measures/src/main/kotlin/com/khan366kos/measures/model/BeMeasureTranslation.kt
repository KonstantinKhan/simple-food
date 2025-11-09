package com.khan366kos.measures.model

import com.khan366kos.common.model.BeId

/**
 * Translation for a measure in a specific locale.
 *
 * @property measureId ID of the measure this translation belongs to
 * @property locale Locale code (e.g., "en", "ru", "fr")
 * @property measureName Full name of the measure in this locale
 * @property measureShortName Short abbreviation in this locale
 */
data class BeMeasureTranslation(
    val measureId: BeId = BeId.NONE,
    val locale: String = "",
    val measureName: String = "",
    val measureShortName: String = ""
) {
    companion object {
        val NONE = BeMeasureTranslation()
    }
}
