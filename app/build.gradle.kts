plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.application")
    id("mediproject.android.application.compose")
    id("mediproject.android.hilt")
}

android {
    defaultConfig {
        applicationId = "com.android.mediproject"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":feature:home"))
    implementation(project(":feature:intro"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:search"))

    api(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.workManagers)
    androidTestImplementation(libs.bundles.testUIs)
    testImplementation(libs.bundles.testUIs)
    androidTestUtil(libs.androidx.test.orchestrator)
}
