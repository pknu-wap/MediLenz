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

    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.materials)

    implementation(libs.bundles.composes)
}