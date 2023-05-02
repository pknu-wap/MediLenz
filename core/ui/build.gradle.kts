plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.material.main)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.materials)
}