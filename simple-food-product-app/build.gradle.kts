plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.khan366kos"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation(project(":simple-food-common-models"))
    implementation(project(":simple-food-transport-models"))
    implementation(project(":simple-food-transport-mappers"))
    implementation(project(":simple-food-product-repo-memory"))
    implementation(project(":simple-food-product-repo-postgresql"))
    implementation(project(":simple-food-repo-measure-memory"))
    implementation(project(":simple-food-repo-measure-postgres"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}


