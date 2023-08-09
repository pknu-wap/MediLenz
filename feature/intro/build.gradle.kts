plugins {
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.intro"

    hilt {
        enableAggregatingTask = true
    }
}


dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:test"))
    implementation(project(":core:data"))

    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)

}
