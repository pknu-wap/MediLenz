plugins {
    id("mediproject.android.application")
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
    }

    namespace = "com.android.mediproject"
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(libs.androidx.appcompat)
}
