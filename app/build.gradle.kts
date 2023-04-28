plugins {
    id("mediproject.android.application")
    id("mediproject.android.application.compose")
    id("mediproject.android.hilt")

    kotlin("android")
}

android {
    defaultConfig {
        applicationId = "com.android.mediproject"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    namespace = "com.android.mediproject"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":feature:interestedmedicine"))
    implementation(project(":feature:splash"))


    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.kotlins)
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    kapt("androidx.hilt:hilt-compiler:1.0.0")

}
