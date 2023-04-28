plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("mediproject.android.feature")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.mediproject.feature.mypage"
}
hilt {
    enableAggregatingTask = true
}
dependencies {
    implementation(libs.bundles.glides)
}