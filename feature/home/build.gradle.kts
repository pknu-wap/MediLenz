plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.android.mediproject.feature.home"
    
    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:search"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:penalties"))

    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
}