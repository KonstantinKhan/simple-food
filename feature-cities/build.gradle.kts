plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.khan366kos"
version = "0.0.1"

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.postgresql)
    implementation(libs.h2)
}


