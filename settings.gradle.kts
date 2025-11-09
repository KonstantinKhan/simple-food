rootProject.name = "simple-food"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":simple-food-product-app")
include(":transport-models")
include(":simple-food-common-models")
include(":transport-mappers")
include(":repo-in-memory")
include(":repo-postgresql")
include(":measures")
