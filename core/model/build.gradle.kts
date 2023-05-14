plugins {
    id(libs.plugins.kotlin.android.get().pluginId)
    id("mediproject.android.feature")
    id("kotlinx-serialization")
}

android {
    namespace = "com.android.mediproject.core.model"

    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}



dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}