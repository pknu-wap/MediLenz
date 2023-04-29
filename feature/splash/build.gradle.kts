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
    implementation(libs.bundles.glides)

}