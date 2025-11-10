package com.khan366kos.routes

import com.khan366kos.common.model.BeId
import com.khan366kos.common.model.BeProduct
import com.khan366kos.common.repository.DbProductFilterRequest
import com.khan366kos.common.repository.DbProductIdRequest
import com.khan366kos.common.repository.DbProductRequest
import com.khan366kos.common.repository.IRepoProduct
import com.khan366kos.mapper.toContext.toContext
import com.khan366kos.mapper.toTransport.toMeasureTranslation
import com.khan366kos.transport.model.Error
import com.khan366kos.transport.model.Product
import com.khan366kos.transport.model.ProductSearchRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.productRoutes(repository: IRepoProduct) {

    route("/products") {
        get {
            val response = repository.products()
            call.respond(response.result.map { it.toMeasureTranslation() })
        }

        post {
            try {
                val transportProduct = call.receive<Product>()
                val beProduct = transportProduct.toContext()
                val response = repository.newProduct(DbProductRequest(beProduct))
                if (response.isSuccess) {
                    call.respond(HttpStatusCode.Created, response.result.toMeasureTranslation())
                } else {
                    call.respond(
                            HttpStatusCode.BadRequest,
                            Error(code = "BAD_REQUEST", message = "Failed to create product")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Invalid product data: ${e.message}")
                )
            }
        }

        post("/search") {
            try {
                val searchRequest = call.receive<ProductSearchRequest>()
                val response = repository.foundProducts(DbProductFilterRequest(searchRequest.query))
                call.respond(response.result.map { it.toMeasureTranslation() })
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

        get("/{id}") {
            val idParam =
                    call.parameters["id"]
                            ?: run {
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
                val response = repository.product(DbProductIdRequest(productId))

                if (response.isSuccess && response.result != BeProduct.NONE) {
                    call.respond(response.result.toMeasureTranslation())
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

        put("/{id}") {
            val idParam =
                    call.parameters["id"]
                            ?: run {
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
                val transportProduct = call.receive<Product>()
                val beProduct = transportProduct.toContext().copy(productId = BeId(uuid))
                val response = repository.updatedProduct(DbProductRequest(beProduct))

                if (response.isSuccess && response.result != BeProduct.NONE) {
                    call.respond(response.result.toMeasureTranslation())
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
            } catch (e: Exception) {
                call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Invalid product data: ${e.message}")
                )
            }
        }

        delete("/{id}") {
            val idParam =
                    call.parameters["id"]
                            ?: run {
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
                val response = repository.deletedProduct(DbProductIdRequest(productId))

                if (response.isSuccess && response.result != BeProduct.NONE) {
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
