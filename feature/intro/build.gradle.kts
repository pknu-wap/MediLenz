plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.android.mediproject.feature.intro"
}

dependencies {
    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(mapOf("path" to ":core:ui")))

    implementation(libs.bundles.uiAndroidx)
}
