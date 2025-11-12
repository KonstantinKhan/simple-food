package com.khan366kos.repository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

/**
 * Database initializer for bootstrapping PostgreSQL connection with HikariCP pooling and Flyway migrations.
 *
 * This is not a factory pattern - it initializes global database state rather than creating instances.
 */
object DatabaseInitializer {

    /**
     * Initialize database connection with the given configuration
     *
     * @param jdbcUrl JDBC connection URL (e.g., "jdbc:postgresql://localhost:5432/simplefood")
     * @param driver JDBC driver class name (e.g., "org.postgresql.Driver")
     * @param user Database username
     * @param password Database password
     * @param maxPoolSize Maximum number of connections in the pool
     */
    fun init(
        jdbcUrl: String,
        driver: String,
        user: String,
        password: String,
        maxPoolSize: Int = 10
    ) {
        val dataSource = createHikariDataSource(jdbcUrl, driver, user, password, maxPoolSize)
        runMigrations(dataSource)
        Database.connect(dataSource)
    }

    /**
     * Create HikariCP data source with connection pooling
     */
    private fun createHikariDataSource(
        jdbcUrl: String,
        driver: String,
        user: String,
        password: String,
        maxPoolSize: Int
    ): HikariDataSource {
        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.driverClassName = driver
            this.username = user
            this.password = password
            this.maximumPoolSize = maxPoolSize
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    /**
     * Run Flyway migrations on the database
     */
    private fun runMigrations(dataSource: DataSource) {
        val flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .load()

        flyway.migrate()
    }
}
