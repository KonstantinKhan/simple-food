package com.khan366kos.common.repository

import com.khan366kos.common.model.BeProduct

data class DbProductsResponse(
    override val isSuccess: Boolean,
    override val result: List<BeProduct>
) : IDbResponse<List<BeProduct>>

