package com.khan366kos.common.model.simple

import com.khan366kos.common.model.BeMeasure

data class BeFats(
    val title: String,
    val shortTitle: String,
    val measure: BeMeasure
) {
    companion object {
        val NONE = BeFats(
            title = "",
            shortTitle = "",
            measure = BeMeasure.NONE
        )
    }
}


