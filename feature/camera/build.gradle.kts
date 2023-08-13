plugins {
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.camera"

    hilt {
        enableAggregatingTask = true
    }

    androidResources {
        noCompress += listOf("tflite")
    }

    @Suppress("UnstableApiUsage") buildFeatures {
        mlModelBinding = true
    }/*

            externalNativeBuild {
                cmake {
                    version = "3.22.1"
                    path = file("src/main/jni/CMakeLists.txt")
                }
            }

            defaultConfig {

                ndk {
                    version = "25.2.9519653"
                    abiFilters += listOf(
                        "armeabi-v7a", "arm64-v8a"
                    )
                }
            }

         */

}



dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":feature:search"))
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.bundles.cameras)

    implementation(libs.androidx.paging.runtime)

    implementation(libs.bundles.navigations)
    implementation(libs.bundles.lifecycles)
    implementation(libs.photo.view)

    implementation(libs.bundles.tflite)
    implementation(libs.smartdeeplink.core)
    implementation(libs.simpledialog)
}
