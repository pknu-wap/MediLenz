plugins {
    id("mediproject.android.library")
    id(libs.plugins.kotlin.serialization.get().pluginId)
    id(libs.plugins.nav.safeargs.kotlin.get().pluginId)
}

android {
    namespace = "com.android.mediproject.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.reflection)
    implementation(libs.androidx.navigation.ui.ktx)
}