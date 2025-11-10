package com.khan366kos.common.model.measure

import com.khan366kos.common.model.BeId
import java.time.Instant

/**
 * Measure domain model representing a unit of measurement.
 *
 * @property id Unique identifier for the measure
 * @property code Unique code for the measure (e.g., "GRAM", "KILOGRAM", "LITER")
 * @property createdAt Timestamp when the measure was created
 */
data class BeMeasure(
    val id: BeId = BeId.NONE,
    val code: String = "",
    val createdAt: Instant = Instant.EPOCH
) {
    companion object {
        val NONE = BeMeasure()
    }
}
