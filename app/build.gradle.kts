plugins {
    id("mediproject.android.application")
    id("mediproject.android.application.compose")
    id("mediproject.android.hilt")

    kotlin("android")
}

android {
    defaultConfig {
        applicationId = "com.android.mediproject"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    namespace = "com.android.mediproject"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":feature:interestedmedicine"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:home"))
    implementation(project(":feature:community"))

    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.navigations)
    implementation(libs.bundles.workManagers)
}
