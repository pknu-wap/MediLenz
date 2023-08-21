plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.android.mediproject.core.ai"
    androidResources {
        noCompress += listOf("tflite")
    }

    @Suppress("UnstableApiUsage") buildFeatures {
        mlModelBinding = true
    }

    @Suppress("UnstableApiUsage") testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.cameras)
    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.tflite)
    ksp(libs.ksealedbinding.compiler)
    implementation(libs.ksealedbinding.annotation)
    implementation(libs.bundles.pytorch)
    testImplementation(libs.bundles.testAndroid)
}
