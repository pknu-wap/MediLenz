plugins {
    id("mediproject.android.library")
    id("mediproject.android.feature")
    id("mediproject.android.hilt")
}

android {
    namespace = "com.android.mediproject.core.ui"
}

dependencies {
    implementation(libs.material.main)

}