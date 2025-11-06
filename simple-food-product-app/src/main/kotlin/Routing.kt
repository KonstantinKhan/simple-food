package com.khan366kos

import com.khan366kos.repository.ProductRepository
import com.khan366kos.routes.productRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val productRepository = ProductRepository()
    
    routing {
        get("/") {
            call.respondText("Simple Food API - Products")
        }
        
        productRoutes(productRepository)
    }
}


