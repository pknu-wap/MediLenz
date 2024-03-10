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
    implementation(project(":feature:aws"))

    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.aws.android.sdk.cognito)

}
