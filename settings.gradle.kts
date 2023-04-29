pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "mediproject"

include(":app")
include(":core:common")
include(":core:data")
include(":core:ui")
include(":feature:interestedmedicine")
include(":feature:splash")
include(":feature:intro")
include(":core:model")
include(":core:network")
include(":core:datastore")
include(":feature:home")
include(":feature:comments")
include(":feature:search")
include(":feature:penalties")
