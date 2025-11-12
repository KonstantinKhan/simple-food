package com.khan366kos.common.model.product.repository

import com.khan366kos.common.interfaces.IDbResponse
import com.khan366kos.common.model.product.BeProduct

data class DbProductsResponse(
    override val isSuccess: Boolean,
    override val result: List<BeProduct>
) : IDbResponse<List<BeProduct>>

