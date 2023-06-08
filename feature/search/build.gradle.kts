plugins {

    id("mediproject.android.feature")

}

android {
    namespace = "com.android.mediproject.feature.search"
    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))

    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.android.flexbox)
    implementation(libs.androidx.paging.runtime)
    kapt(libs.bundles.glides.kapt)

}