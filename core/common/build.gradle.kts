plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
}

android {
    namespace = "com.android.mediproject.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}