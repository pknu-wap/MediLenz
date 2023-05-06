plugins {
    id(libs.plugins.kotlin.android.get().pluginId)
    id("mediproject.android.feature")
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.android.mediproject.core.ui"
    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
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