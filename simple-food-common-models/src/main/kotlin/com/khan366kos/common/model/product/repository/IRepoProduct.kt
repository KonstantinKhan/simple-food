package com.khan366kos.common.model.product.repository

interface IRepoProduct {
    fun products(): DbProductsResponse
    fun product(request: DbProductIdRequest): DbProductResponse
    fun newProduct(request: DbProductRequest): DbProductResponse
    fun updatedProduct(request: DbProductRequest): DbProductResponse
    fun deletedProduct(request: DbProductIdRequest): DbProductResponse
    fun foundProducts(request: DbProductFilterRequest): DbProductsResponse
}