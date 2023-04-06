plugins {
    id("mediproject.android.application")
    id("mediproject.android.hilt")
    id("org.jetbrains.kotlin.android")
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
    implementation(project(":feature:interestedmedicine"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.material.main)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.ktx)
}
