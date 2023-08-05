pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://www.jitpack.io") }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
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
include(":core:database")