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
include(":core:database")
include(":core:domain")
include(":core:annotation")
include(":core:compiler")
include(":core:test")
include(":test")

include(":feature:favoritemedicine")
include(":feature:intro")
include(":feature:home")
include(":feature:comments")
include(":feature:search")
include(":feature:mypage")
include(":feature:etc")
include(":feature:medicine")

include(":feature:news")
include(":feature:camera")
include(":core:ai")
include(":feature:aws")
