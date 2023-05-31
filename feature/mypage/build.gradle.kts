plugins {

    id("mediproject.android.feature")
    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.android.mediproject.feature.mypage"

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
    implementation(project(":core:model"))
    implementation(project(":feature:interestedmedicine"))
    implementation(project(":feature:comments"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
}