plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.interestedmedicine"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.glides)

}
