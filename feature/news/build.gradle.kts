plugins {
    id("mediproject.android.feature.compose")
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.android.mediproject.feature.news"

    hilt {
        enableAggregatingTask = true
    }
}


dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(libs.bundles.glides)
    implementation(libs.glide.compose)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)

    implementation(libs.bundles.composes)
    debugImplementation(libs.bundles.compose.debug)

    kapt(libs.bundles.glides.kapt)

    ksp(libs.ksealedbinding.compiler)
    implementation(libs.ksealedbinding.annotation)
}
