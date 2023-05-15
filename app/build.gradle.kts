plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.application")
    id("mediproject.android.hilt")
    id("androidx.navigation.safeargs.kotlin")
    id(libs.plugins.kapt.get().pluginId)
}

android {
    defaultConfig {
        applicationId = "com.android.mediproject"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        //  testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.android.mediproject"

    buildFeatures {
        buildConfig = true
    }

    lint {
        checkDependencies = true
        ignoreTestSources = true
    }


}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))


    implementation(project(":feature:interestedmedicine"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:home"))
    implementation(project(":feature:intro"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:search"))
    implementation(project(":feature:mypage"))

    implementation(project(":feature:setting"))
    implementation(project(":feature:penalties"))
    implementation(project(":feature:medicine"))
    implementation(project(":feature:news"))
    implementation(project(":feature:camera"))

    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.navigations)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.workManagers)
    implementation(libs.bundles.glides)
    kapt(libs.bundles.glides.kapt)

    /*
    androidTestImplementation(libs.bundles.testUIs)
    testImplementation(libs.bundles.testUIs)
    androidTestUtil(libs.androidx.test.orchestrator)

     */
}