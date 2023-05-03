plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id("kotlinx-serialization")
}

android {
    namespace = "com.android.mediproject.core.common"

    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.reflection)
}