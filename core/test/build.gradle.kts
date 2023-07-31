plugins {
    id("mediproject.android.library")
    id("kotlinx-serialization")
    id("mediproject.android.hilt")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.android.mediproject.core.test"

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {

    implementation(libs.bundles.testLocal)
    implementation(libs.bundles.testAndroid)

}
