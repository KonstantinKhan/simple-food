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

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
}
