plugins {

    id("mediproject.android.feature.compose")
    alias(libs.plugins.kotlin.parcelize)

}

android {
    namespace = "com.android.mediproject.feature.news"
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
    implementation(project(":core:domain"))
    implementation(libs.bundles.glides)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.composes)
    implementation(libs.kotlinx.datetime)
    kapt(libs.bundles.glides.kapt)
}