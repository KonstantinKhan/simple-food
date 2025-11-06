group = "com.khan366kos"
version = "0.0.1"

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor) apply false
    id("org.openapi.generator") version "7.7.0" apply false
}
