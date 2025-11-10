package com.khan366kos.repository

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeProduct
import com.khan366kos.common.repository.*
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class ProductRepository : IRepoProduct {
    private val products = ConcurrentHashMap<BeId, BeProduct>()

    init {
        TestData.createSampleProducts().forEach { product -> products[product.productId] = product }
    }

    override fun products(): DbProductsResponse {
        val allProducts = products.values.toList()
        return DbProductsResponse(isSuccess = true, result = allProducts)
    }

    override fun product(request: DbProductIdRequest): DbProductResponse {
        val product = products[request.id]
        return if (product != null) {
            DbProductResponse(isSuccess = true, result = product)
        } else {
            DbProductResponse(isSuccess = false, result = BeProduct.NONE)
        }
    }

    override fun newProduct(request: DbProductRequest): DbProductResponse {
        val generatedId = BeId(UUID.randomUUID())
        val productWithId = request.product.copy(productId = generatedId)
        products[generatedId] = productWithId
        return DbProductResponse(isSuccess = true, result = productWithId)
    }

    override fun updatedProduct(request: DbProductRequest): DbProductResponse {
        val id = request.product.productId
        return if (products.containsKey(id)) {
            products[id] = request.product
            DbProductResponse(isSuccess = true, result = request.product)
        } else {
            DbProductResponse(isSuccess = false, result = BeProduct.NONE)
        }
    }

    override fun deletedProduct(request: DbProductIdRequest): DbProductResponse {
        val product = products.remove(request.id)
        return if (product != null) {
            DbProductResponse(isSuccess = true, result = product)
        } else {
            DbProductResponse(isSuccess = false, result = BeProduct.NONE)
        }
    }

    override fun foundProducts(request: DbProductFilterRequest): DbProductsResponse {
        val lowerQuery = request.searchStr.lowercase()
        val foundProducts =
                products.values.filter { product ->
                    product.productName.lowercase().contains(lowerQuery) ||
                            product.categories.value.any {
                                it.value.lowercase().contains(lowerQuery)
                            }
                }
        return DbProductsResponse(isSuccess = true, result = foundProducts)
    }
}
