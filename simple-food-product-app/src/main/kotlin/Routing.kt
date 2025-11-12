package com.khan366kos

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.interfaces.IRepoProduct
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.measures.repository.DbMeasureRequest
import com.khan366kos.measures.repository.IRepoMeasure
import com.khan366kos.measures.repository.MeasureRepositoryInMemory
import com.khan366kos.measures.repository.MeasureRepositoryPostgres
import com.khan366kos.repository.ProductRepository
import com.khan366kos.repository.ProductRepositoryPostgres
import com.khan366kos.routes.measureRoutes
import com.khan366kos.routes.productRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Instant
import java.util.*

fun Application.configureRouting() {
    val repositoryType = environment.config.propertyOrNull("repository.type")?.getString() ?: "memory"

    val productRepository: IRepoProduct = when (repositoryType) {
        "postgres" -> ProductRepositoryPostgres()
        else -> ProductRepository()
    }

    val measureRepository: IRepoMeasure = when (repositoryType) {
        "postgres" -> MeasureRepositoryPostgres()
        else -> {
            val repo = MeasureRepositoryInMemory()
            // Seed default measures for in-memory mode
            seedDefaultMeasures(repo)
            repo
        }
    }

    routing {
        get("/") {
            call.respondText("Simple Food API - Products & Measures")
        }

        productRoutes(productRepository)
        measureRoutes(measureRepository)
    }
}

/**
 * Seed default measures with English and Russian translations for in-memory repository
 */
private fun seedDefaultMeasures(repository: IRepoMeasure) {
    val measures = listOf(
        // GRAM
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                code = "GRAM",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = "ru",
                    name = "грамм",
                    shortName = "г"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = "en",
                    name = "gram",
                    shortName = "g"
                )
            )
        ),
        // KILOGRAM
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                code = "KILOGRAM",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = "ru",
                    name = "килограмм",
                    shortName = "кг"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = "en",
                    name = "kilogram",
                    shortName = "kg"
                )
            )
        ),
        // LITER
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                code = "LITER",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = "ru",
                    name = "литр",
                    shortName = "л"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = "en",
                    name = "liter",
                    shortName = "l"
                )
            )
        ),
        // MILLILITER
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                code = "MILLILITER",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    locale = "ru",
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    name = "миллилитр",
                    shortName = "мл"
                ),
                BeMeasureTranslation(
                    locale = "en",
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    name = "milliliter",
                    shortName = "ml"
                )
            )
        ),
        // PIECE
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                code = "PIECE",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = "ru",
                    name = "штука",
                    shortName = "шт"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = "en",
                    name = "piece",
                    shortName = "pc"
                )
            )
        ),
        // SERVING
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                code = "SERVING",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = "ru",
                    name = "порция",
                    shortName = "порц"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = "en",
                    name = "serving",
                    shortName = "serv"
                )
            )
        ),
        // KILOCALORIE
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                code = "KILOCALORIE",
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = "ru",
                    name = "килокалория",
                    shortName = "ккал"
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = "en",
                    name = "kilocalorie",
                    shortName = "kcal"
                )
            )
        )
    )

    repository.seedMeasures(measures)
}


