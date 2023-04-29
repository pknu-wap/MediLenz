plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")

}

android {
    namespace = "com.android.mediproject.feature.home"
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:search"))
    implementation(project(":feature:penalties"))
    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.glides)
}
