plugins {
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
    implementation(project(":core:model"))
    implementation(project(":feature:search"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:penalties"))
    implementation(project(":feature:camera"))

    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
}