package com.khan366kos.plugins

import com.khan366kos.transport.model.Error
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                Error(
                    code = "INTERNAL_SERVER_ERROR",
                    message = cause.message ?: "Unknown error occurred"
                )
            )
        }
        
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                Error(
                    code = "BAD_REQUEST",
                    message = cause.message ?: "Invalid request"
                )
            )
        }
    }
}

