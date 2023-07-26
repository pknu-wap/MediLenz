plugins {
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.comments"

    hilt {
        enableAggregatingTask = true
    }

}


dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.androidx.paging.runtime)

}
