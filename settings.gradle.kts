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
include(":core")
include(":core:common")
include(":core:data")
include(":core:ui")
include(":core:model")
include(":core:network")
include(":core:datastore")
include(":core:domain")

include(":feature:interestedmedicine")
include(":feature:splash")
include(":feature:intro")
include(":feature:home")
include(":feature:comments")
include(":feature:search")
include(":feature:penalties")
include(":feature:mypage")
include(":feature:setting")
include(":feature:medicine")

include(":feature:news")
include(":feature:camera")