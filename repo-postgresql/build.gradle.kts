plugins {
    kotlin("jvm")
}

group = "com.khan366kos"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":common-models"))
    implementation(project(":measures"))

    // Exposed ORM
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)

    // PostgreSQL driver
    implementation(libs.postgresql)

    // Flyway migrations
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // HikariCP connection pooling
    implementation(libs.hikari)

    // Logging
    implementation(libs.logback.classic)

    // Testing
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
}
