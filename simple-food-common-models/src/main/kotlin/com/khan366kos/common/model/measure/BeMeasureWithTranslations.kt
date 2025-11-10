package com.khan366kos.common.model.measure

/**
 * Measure with all its translations.
 *
 * @property measure The measure entity
 * @property translations List of translations for different locales
 */
data class BeMeasureWithTranslations(
    val measure: BeMeasure = BeMeasure.NONE,
    val translations: List<BeMeasureTranslation> = emptyList()
) {
    companion object {
        val NONE = BeMeasureWithTranslations()
    }

    /**
     * Get translation for a specific locale.
     * Falls back to first available translation if locale not found.
     *
     * @param locale Desired locale code
     * @return Translation for the locale, or first available, or NONE
     */
    fun getTranslation(locale: String): BeMeasureTranslation {
        return translations.find { it.locale == locale }
            ?: translations.firstOrNull()
            ?: BeMeasureTranslation.NONE
    }

    /**
     * Get translation for a specific locale with fallback.
     *
     * @param locale Preferred locale
     * @param fallbackLocale Fallback locale if preferred not found
     * @return Translation for the locale, fallback locale, or first available
     */
    fun getTranslation(locale: String, fallbackLocale: String): BeMeasureTranslation {
        return translations.find { it.locale == locale }
            ?: translations.find { it.locale == fallbackLocale }
            ?: translations.firstOrNull()
            ?: BeMeasureTranslation.NONE
    }
}
