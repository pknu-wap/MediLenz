plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.search"

}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.android.flexbox)
}