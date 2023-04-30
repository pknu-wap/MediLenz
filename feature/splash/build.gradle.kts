plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")

}

android {
    namespace = "com.android.mediproject.feature.splash"
}
hilt {
    enableAggregatingTask = true
}
dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.lifecycles)
}