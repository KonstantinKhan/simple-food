package com.khan366kos.routes

import com.khan366kos.common.model.BeId
import com.khan366kos.mapper.toContext.toContext
import com.khan366kos.mapper.toTransport.toTransport
import com.khan366kos.repository.ProductRepository
import com.khan366kos.transport.model.Error
import com.khan366kos.transport.model.Product
import com.khan366kos.transport.model.ProductSearchRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.productRoutes(repository: ProductRepository) {
    
    route("/products") {
        
        // GET /products - Список всех продуктов
        get {
            val products = repository.findAll()
            call.respond(products.map { it.toTransport() })
        }
        
        // POST /products - Создание продукта
        post {
            try {
                val transportProduct = call.receive<Product>()
                val beProduct = transportProduct.toContext()
                val created = repository.create(beProduct)
                call.respond(HttpStatusCode.Created, created.toTransport())
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid product data: ${e.message}"
                    )
                )
            }
        }
        
        // POST /products/search - Поиск продуктов
        post("/search") {
            try {
                val searchRequest = call.receive<ProductSearchRequest>()
                val results = repository.search(searchRequest.query)
                call.respond(results.map { it.toTransport() })
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid search request: ${e.message}"
                    )
                )
            }
        }
        
        // GET /products/{id} - Получение продукта по ID
        get("/{id}") {
            val idParam = call.parameters["id"]
            if (idParam == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Product ID is required"
                    )
                )
                return@get
            }
            
            try {
                val uuid = UUID.fromString(idParam)
                val productId = BeId(uuid)
                val product = repository.findById(productId)
                
                if (product == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(
                            code = "NOT_FOUND",
                            message = "Product with id $idParam not found"
                        )
                    )
                } else {
                    call.respond(product.toTransport())
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid product ID format: ${e.message}"
                    )
                )
            }
        }
        
        // PUT /products/{id} - Обновление продукта
        put("/{id}") {
            val idParam = call.parameters["id"]
            if (idParam == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Product ID is required"
                    )
                )
                return@put
            }
            
            try {
                val uuid = UUID.fromString(idParam)
                val productId = BeId(uuid)
                val transportProduct = call.receive<Product>()
                val beProduct = transportProduct.toContext()
                
                val updated = repository.update(productId, beProduct)
                if (updated == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(
                            code = "NOT_FOUND",
                            message = "Product with id $idParam not found"
                        )
                    )
                } else {
                    call.respond(updated.toTransport())
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid product ID format: ${e.message}"
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid product data: ${e.message}"
                    )
                )
            }
        }
        
        // DELETE /products/{id} - Удаление продукта
        delete("/{id}") {
            val idParam = call.parameters["id"]
            if (idParam == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Product ID is required"
                    )
                )
                return@delete
            }
            
            try {
                val uuid = UUID.fromString(idParam)
                val productId = BeId(uuid)
                
                val deleted = repository.delete(productId)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(
                            code = "NOT_FOUND",
                            message = "Product with id $idParam not found"
                        )
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(
                        code = "BAD_REQUEST",
                        message = "Invalid product ID format: ${e.message}"
                    )
                )
            }
        }
    }
}


