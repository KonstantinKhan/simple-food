package com.khan366kos.routes

import com.khan366kos.common.model.common.BeId
import com.khan366kos.mapper.toContext.toContext
import com.khan366kos.mapper.toTransport.toMeasureTranslation
import com.khan366kos.common.model.measure.repository.DbMeasureCodeRequest
import com.khan366kos.common.model.measure.repository.DbMeasureFilterRequest
import com.khan366kos.common.model.measure.repository.DbMeasureIdRequest
import com.khan366kos.common.model.measure.repository.DbMeasureRequest
import com.khan366kos.common.model.measure.repository.IRepoMeasure
import com.khan366kos.transport.model.Error
import com.khan366kos.transport.model.MeasureDetail
import com.khan366kos.transport.model.MeasureDetailCreateRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.measureRoutes(repository: IRepoMeasure) {

    route("/measures") {
        get {
            val locale = call.request.queryParameters["locale"]
            val search = call.request.queryParameters["search"]

            val response = if (locale != null || search != null) {
                repository.foundMeasures(DbMeasureFilterRequest(locale = locale, searchText = search))
            } else {
                repository.measures()
            }

            call.respond(response.result.map { it.toMeasureTranslation() })
        }

        post {
            try {
                val createRequest = call.receive<MeasureDetailCreateRequest>()
                val beMeasure = createRequest.toContext()
                val response = repository.newMeasure(
                    DbMeasureRequest(
                        measure = beMeasure.measure,
                        translations = beMeasure.translations
                    )
                )
                if (response.isSuccess) {
                    call.respond(HttpStatusCode.Created, response.result.toMeasureTranslation())
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Failed to create measure (code may already exist)")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(code = "BAD_REQUEST", message = "Invalid measure data: ${e.message}")
                )
            }
        }

        get("/{id}") {
            val idParam = call.parameters["id"]
                ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Measure ID is required")
                    )
                    return@get
                }

            try {
                val uuid = UUID.fromString(idParam)
                val response = repository.measure(DbMeasureIdRequest(BeId(uuid)))
                if (response.isSuccess) {
                    call.respond(response.result.toMeasureTranslation())
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(code = "NOT_FOUND", message = "Measure not found")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(code = "BAD_REQUEST", message = "Invalid UUID format")
                )
            }
        }

        put("/{id}") {
            val idParam = call.parameters["id"]
                ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Measure ID is required")
                    )
                    return@put
                }

            try {
                val transportMeasure = call.receive<MeasureDetail>()
                val beMeasure = transportMeasure.toContext()

                // Ensure ID from path matches ID in body
                val pathUuid = UUID.fromString(idParam)
                if (beMeasure.measure.id.value != pathUuid.toString()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "ID in path does not match ID in body")
                    )
                    return@put
                }

                val response = repository.updatedMeasure(
                    DbMeasureRequest(
                        measure = beMeasure.measure,
                        translations = beMeasure.translations
                    )
                )
                if (response.isSuccess) {
                    call.respond(response.result.toMeasureTranslation())
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(code = "NOT_FOUND", message = "Measure not found")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(code = "BAD_REQUEST", message = "Invalid UUID format")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(code = "BAD_REQUEST", message = "Invalid measure data: ${e.message}")
                )
            }
        }

        delete("/{id}") {
            val idParam = call.parameters["id"]
                ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Measure ID is required")
                    )
                    return@delete
                }

            try {
                val uuid = UUID.fromString(idParam)
                val response = repository.deletedMeasure(DbMeasureIdRequest(BeId(uuid)))
                if (response.isSuccess) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Error(code = "NOT_FOUND", message = "Measure not found")
                    )
                }
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Error(code = "BAD_REQUEST", message = "Invalid UUID format")
                )
            }
        }

        get("/by-code/{code}") {
            val code = call.parameters["code"]
                ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Error(code = "BAD_REQUEST", message = "Measure code is required")
                    )
                    return@get
                }

            val response = repository.measureByCode(DbMeasureCodeRequest(code))
            if (response.isSuccess) {
                call.respond(response.result.toMeasureTranslation())
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    Error(code = "NOT_FOUND", message = "Measure not found")
                )
            }
        }
    }
}
