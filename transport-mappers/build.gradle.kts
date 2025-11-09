plugins {
    kotlin("jvm")
}

group = "com.khan366kos"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common-models"))
    implementation(project(":transport-models"))
    implementation(project(":measures"))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}


