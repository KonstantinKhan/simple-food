package com.khan366kos.common.model.measure.repository

import com.khan366kos.common.model.common.BeLocale
import com.khan366kos.common.model.common.BeSearchString

data class DbMeasureFilterRequest(
    val locale: BeLocale = BeLocale.NONE,
    val searchText: BeSearchString = BeSearchString.NONE
)
