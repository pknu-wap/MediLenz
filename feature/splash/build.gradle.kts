plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
}

android {
    namespace = "com.android.mediproject.feature.splash"
}

dependencies {
    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(mapOf("path" to ":core:ui")))
}