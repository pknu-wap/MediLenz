plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")

}

android {
    namespace = "com.android.mediproject.feature.intro"
}

dependencies {
    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(mapOf("path" to ":core:ui")))

    implementation("androidx.constraintlayout:constraintlayout")

}