plugins {

    id("mediproject.android.feature")
    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.android.mediproject.feature.mypage"

    hilt {
        enableAggregatingTask = true
    }
}


dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:test"))
    implementation(project(":feature:favoritemedicine"))
    implementation(project(":feature:comments"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)

    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.lottie)
}
