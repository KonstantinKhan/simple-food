package com.khan366kos.repository

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeProduct
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory репозиторий для хранения продуктов
 */
class ProductRepository {
    private val products = ConcurrentHashMap<BeId, BeProduct>()
    
    init {
        // Инициализируем репозиторий с тестовыми данными
        TestData.createSampleProducts().forEach { product ->
            products[product.productId] = product
        }
    }

    fun findAll(): List<BeProduct> {
        return products.values.toList()
    }

    fun findById(id: BeId): BeProduct? {
        return products[id]
    }

    fun create(product: BeProduct): BeProduct {
        products[product.productId] = product
        return product
    }

    fun update(id: BeId, product: BeProduct): BeProduct? {
        if (!products.containsKey(id)) {
            return null
        }
        products[id] = product
        return product
    }

    fun delete(id: BeId): Boolean {
        return products.remove(id) != null
    }

    fun search(query: String): List<BeProduct> {
        val lowerQuery = query.lowercase()
        return products.values.filter { product ->
            product.productName.lowercase().contains(lowerQuery) ||
            product.categories.value.any { it.value.lowercase().contains(lowerQuery) }
        }
    }
}

