rootProject.name = "simple-food"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":simple-food-product-app")
include(":simple-food-transport-models")
include(":simple-food-common-models")
include(":simple-food-transport-mappers")
include(":simple-food-repo-in-memory")
include(":simple-food-repo-postgresql")
include(":simple-food-repo-measure-memory")
include(":simple-food-repo-measure-postgres")
