plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id("androidx.navigation.safeargs.kotlin")
    id(libs.plugins.ksp.get().pluginId)
}

android {
    namespace = "com.android.mediproject.feature.camera"
    buildFeatures {
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            version = "3.10.2"
            path = file("src/main/jni/CMakeLists.txt")
        }
    }

    ndkVersion = "21.3.6528147"
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
    implementation(libs.bundles.cameras)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.kotlinx.datetime)

    kapt(libs.androidx.lifecycle.compilerKapt)
    ksp(libs.glide.ksp)
}