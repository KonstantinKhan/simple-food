package com.khan366kos

import com.khan366kos.repository.DatabaseInitializer
import io.ktor.server.application.*

/**
 * Configure database connection based on repository type
 */
fun Application.configureDatabase() {
    val repositoryType = environment.config.propertyOrNull("repository.type")?.getString() ?: "memory"

    if (repositoryType == "postgres") {
        println("===postgres===")
        val jdbcUrl = environment.config.property("postgres.jdbcUrl").getString()
        val driver = environment.config.property("postgres.driver").getString()
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()
        val maxPoolSize = environment.config.propertyOrNull("postgres.maxPoolSize")?.getString()?.toInt() ?: 10

        DatabaseInitializer.init(
            jdbcUrl = jdbcUrl,
            driver = driver,
            user = user,
            password = password,
            maxPoolSize = maxPoolSize
        )

        log.info("Database initialized with PostgreSQL repository")
    } else {
        log.info("Using in-memory repository")
    }
}
