plugins {
    id("mediproject.android.library")
    id(libs.plugins.kotlin.serialization.get().pluginId)
    id(libs.plugins.nav.safeargs.kotlin.get().pluginId)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.android.mediproject.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflection)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.tensorflow.lite.taskVisionPlayServices)
}
