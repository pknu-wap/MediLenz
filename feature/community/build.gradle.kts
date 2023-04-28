plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.mediproject.feature.community"
}

dependencies {
    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(mapOf("path" to ":core:ui")))

    implementation("androidx.constraintlayout:constraintlayout")
}