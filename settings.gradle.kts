rootProject.name = "simple-food"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":app")
include(":feature-cities")
include(":feature-users")
