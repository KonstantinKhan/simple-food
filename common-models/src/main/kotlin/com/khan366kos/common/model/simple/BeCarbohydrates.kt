package com.khan366kos.common.model.simple

import com.khan366kos.common.model.BeMeasure

data class BeCarbohydrates(
    val title: String,
    val shortTitle: String,
    val measure: BeMeasure
) {
    companion object {
        val NONE = BeCarbohydrates(
            title = "",
            shortTitle = "",
            measure = BeMeasure.NONE
        )
    }
}


