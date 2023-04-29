plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")

}

android {
    namespace = "com.android.mediproject.feature.interestedmedicine"
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.glides)

}
