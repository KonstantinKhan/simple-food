package com.khan366kos.common.model.product.repository

import com.khan366kos.common.model.common.BeSearchString

data class DbProductFilterRequest(
    val searchStr: BeSearchString
)