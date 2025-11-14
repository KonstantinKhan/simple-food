package com.khan366kos

import com.khan366kos.common.model.common.BeId
import com.khan366kos.common.model.common.BeLocale
import com.khan366kos.common.model.product.repository.IRepoProduct
import com.khan366kos.common.model.measure.BeMeasure
import com.khan366kos.common.model.measure.BeMeasureCode
import com.khan366kos.common.model.measure.BeMeasureName
import com.khan366kos.common.model.measure.BeMeasureShortName
import com.khan366kos.common.model.measure.BeMeasureTranslation
import com.khan366kos.common.model.measure.repository.DbMeasureRequest
import com.khan366kos.common.model.measure.repository.IRepoMeasure
import com.khan366kos.measures.repository.memory.MeasureRepositoryInMemory
import com.khan366kos.measures.repository.postgres.MeasureRepositoryPostgres
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
                code = BeMeasureCode("GRAM"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("грамм"),
                    shortName = BeMeasureShortName("г")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("gram"),
                    shortName = BeMeasureShortName("g")
                )
            )
        ),
        // KILOGRAM
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                code = BeMeasureCode("KILOGRAM"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("килограмм"),
                    shortName = BeMeasureShortName("кг")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000002")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("kilogram"),
                    shortName = BeMeasureShortName("kg")
                )
            )
        ),
        // LITER
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                code = BeMeasureCode("LITER"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("литр"),
                    shortName = BeMeasureShortName("л")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000003")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("liter"),
                    shortName = BeMeasureShortName("l")
                )
            )
        ),
        // MILLILITER
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                code = BeMeasureCode("MILLILITER"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    locale = BeLocale("ru"),
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    name = BeMeasureName("миллилитр"),
                    shortName = BeMeasureShortName("мл")
                ),
                BeMeasureTranslation(
                    locale = BeLocale("en"),
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000004")),
                    name = BeMeasureName("milliliter"),
                    shortName = BeMeasureShortName("ml")
                )
            )
        ),
        // PIECE
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                code = BeMeasureCode("PIECE"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("штука"),
                    shortName = BeMeasureShortName("шт")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000005")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("piece"),
                    shortName = BeMeasureShortName("pc")
                )
            )
        ),
        // SERVING
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                code = BeMeasureCode("SERVING"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("порция"),
                    shortName = BeMeasureShortName("порц")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000006")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("serving"),
                    shortName = BeMeasureShortName("serv")
                )
            )
        ),
        // KILOCALORIE
        DbMeasureRequest(
            measure = BeMeasure(
                id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                code = BeMeasureCode("KILOCALORIE"),
                createdAt = Instant.now()
            ),
            translations = listOf(
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = BeLocale("ru"),
                    name = BeMeasureName("килокалория"),
                    shortName = BeMeasureShortName("ккал")
                ),
                BeMeasureTranslation(
                    id = BeId(UUID.fromString("00000000-0000-0000-0000-000000000007")),
                    locale = BeLocale("en"),
                    name = BeMeasureName("kilocalorie"),
                    shortName = BeMeasureShortName("kcal")
                )
            )
        )
    )

    repository.seedMeasures(measures)
}


