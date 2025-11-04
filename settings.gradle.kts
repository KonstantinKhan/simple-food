rootProject.name = "simple-food"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":app")
include(":transport-models")
include(":common-models")
include(":transport-mappers")
