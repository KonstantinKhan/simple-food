package com.khan366kos

import com.khan366kos.common.model.BeId
import com.khan366kos.common.repository.IRepoProduct
import com.khan366kos.measures.model.BeMeasure
import com.khan366kos.measures.model.BeMeasureTranslation
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = "ru",
                    measureName = "грамм",
                    measureShortName = "г"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = "en",
                    measureName = "gram",
                    measureShortName = "g"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = "ru",
                    measureName = "килограмм",
                    measureShortName = "кг"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = "en",
                    measureName = "kilogram",
                    measureShortName = "kg"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = "ru",
                    measureName = "литр",
                    measureShortName = "л"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = "en",
                    measureName = "liter",
                    measureShortName = "l"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    locale = "ru",
                    measureName = "миллилитр",
                    measureShortName = "мл"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    locale = "en",
                    measureName = "milliliter",
                    measureShortName = "ml"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = "ru",
                    measureName = "штука",
                    measureShortName = "шт"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = "en",
                    measureName = "piece",
                    measureShortName = "pc"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = "ru",
                    measureName = "порция",
                    measureShortName = "порц"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = "en",
                    measureName = "serving",
                    measureShortName = "serv"
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
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = "ru",
                    measureName = "килокалория",
                    measureShortName = "ккал"
                ),
                BeMeasureTranslation(
                    measureId = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = "en",
                    measureName = "kilocalorie",
                    measureShortName = "kcal"
                )
            )
        )
    )

    repository.seedMeasures(measures)
}


