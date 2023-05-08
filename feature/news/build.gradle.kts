plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id(libs.plugins.ksp.get().pluginId)
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

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.composes)
    kapt(libs.androidx.lifecycle.compilerKapt)
    ksp(libs.glide.ksp)
}