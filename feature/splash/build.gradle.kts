plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
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