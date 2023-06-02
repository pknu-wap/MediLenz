plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
}

android {
    namespace = "com.android.mediproject.core.database"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.rooms)
    implementation(libs.kotlinx.coroutines.android)
    kapt(libs.androidx.room.compileKsp)
}