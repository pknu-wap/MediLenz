plugins {
    id(libs.plugins.kotlin.android.get().pluginId)
    id("mediproject.android.feature.compose")
    id(libs.plugins.kapt.get().pluginId)
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
    implementation(libs.bundles.glides)

    implementation(libs.bundles.composes)

    kapt(libs.bundles.glides.kapt)

}