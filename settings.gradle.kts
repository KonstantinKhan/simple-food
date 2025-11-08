rootProject.name = "simple-food"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":simple-food-product-app")
include(":transport-models")
include(":common-models")
include(":transport-mappers")
include(":repo-in-memory")
