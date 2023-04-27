plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.android.mediproject.core.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofits)
    implementation(libs.kotlinx.datetime)
}