plugins {
    kotlin("jvm")
}

group = "com.khan366kos"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":simple-food-common-models"))

    // Exposed ORM for PostgreSQL repository
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    // PostgreSQL driver
    implementation(libs.postgresql)

    // HikariCP connection pooling
    implementation(libs.hikari)

    // Logging
    implementation(libs.logback.classic)

    // Testing
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.flyway.core)
    testImplementation(libs.flyway.database.postgresql)
    testRuntimeOnly(project(":simple-food-repo-postgresql")) // For accessing migration files
}

tasks.test {
    useJUnitPlatform()
}
