plugins {
    id("mediproject.android.library")
    id("mediproject.android.library.compose")
}

android {
    namespace = "com.android.mediproject.core.ui"
}

dependencies {
    implementation(libs.material.main)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.core.ktx)
}