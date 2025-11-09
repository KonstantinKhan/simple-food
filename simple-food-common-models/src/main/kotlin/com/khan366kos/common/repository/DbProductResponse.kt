package com.khan366kos.common.repository

import com.khan366kos.common.model.BeProduct

data class DbProductResponse(
    override val isSuccess: Boolean,
    override val result: BeProduct
) : IDbResponse<BeProduct>
