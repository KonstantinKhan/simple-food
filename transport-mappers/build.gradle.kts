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
}

kotlin {
    jvmToolchain(21)
}


