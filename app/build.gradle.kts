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
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":feature:interestedmedicine"))

    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.kotlins)
}
