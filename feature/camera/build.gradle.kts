plugins {
    id("mediproject.android.feature")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.android.mediproject.feature.camera"

    hilt {
        enableAggregatingTask = true
    }

}



dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:ai"))
    implementation(project(":feature:search"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.cameras)
    implementation(libs.tensorflow.lite.taskVisionPlayServices)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.photo.view)
    implementation(libs.lottie)

    implementation(libs.smartdeeplink.core)
    implementation(libs.simpledialog)

    ksp(libs.ksealedbinding.compiler)
    implementation(libs.ksealedbinding.annotation)
}
