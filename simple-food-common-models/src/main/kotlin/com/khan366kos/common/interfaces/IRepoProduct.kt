package com.khan366kos.common.interfaces

import com.khan366kos.common.model.product.repository.DbProductFilterRequest
import com.khan366kos.common.model.product.repository.DbProductIdRequest
import com.khan366kos.common.model.product.repository.DbProductRequest
import com.khan366kos.common.model.product.repository.DbProductResponse
import com.khan366kos.common.model.product.repository.DbProductsResponse

interface IRepoProduct {
    fun products(): DbProductsResponse
    fun product(request: DbProductIdRequest): DbProductResponse
    fun newProduct(request: DbProductRequest): DbProductResponse
    fun updatedProduct(request: DbProductRequest): DbProductResponse
    fun deletedProduct(request: DbProductIdRequest): DbProductResponse
    fun foundProducts(request: DbProductFilterRequest): DbProductsResponse
}
