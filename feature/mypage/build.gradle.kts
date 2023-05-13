plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id(libs.plugins.kapt.get().pluginId)
}

android {
    namespace = "com.android.mediproject.feature.mypage"

    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:interestedmedicine"))
    implementation(project(":feature:comments"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
}