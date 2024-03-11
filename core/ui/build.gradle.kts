plugins {
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.core.ui"

    hilt {
        enableAggregatingTask = true
    }

}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.material.main)

    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.glides)
    implementation(libs.androidx.splash)

    kapt(libs.bundles.glides.kapt)

}
