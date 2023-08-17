import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("mediproject.android.application")
    id("mediproject.android.hilt")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
}

android {
    signingConfigs {
        val properties = Properties()
        properties.load(project.rootProject.file("/apikey.properties").bufferedReader())
        create("release") {
            storeFile = project.rootProject.file(properties["SIGNED_STORE_FILE"] as String)
            storePassword = properties["SIGNED_STORE_PASSWORD"] as String
            keyAlias = properties["SIGNED_KEY_ALIAS"] as String
            keyPassword = properties["SIGNED_KEY_PASSWORD"] as String
        }
    }

    defaultConfig {
        applicationId = "com.android.mediproject"
        versionCode = 1
        versionName = "1.0.0-alpha01"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildTypes {
            debug {

            }
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                    "proguard-glide.pro",
                    "proguard-okhttp3.pro",
                    "proguard-room.pro",
                    "proguard-retrofit2.pro",
                )
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    namespace = "com.android.mediproject"

    lint {
        checkDependencies = true
        ignoreTestSources = true
    }
    hilt {
        enableAggregatingTask = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:ai"))
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:test"))
    implementation(project(":feature:favoritemedicine"))
    implementation(project(":feature:home"))
    implementation(project(":feature:intro"))
    implementation(project(":feature:comments"))
    implementation(project(":feature:search"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:etc"))
    implementation(project(":feature:medicine"))
    implementation(project(":feature:news"))
    implementation(project(":feature:camera"))
    implementation(project(":core:annotation"))
    ksp(project(":core:compiler"))

    implementation(libs.bundles.lifecycles)
    implementation(libs.bundles.materials)
    implementation(libs.bundles.composes)
    implementation(libs.bundles.navigations)
    implementation(libs.bundles.kotlins)
    implementation(libs.bundles.workManagers)
    implementation(libs.bundles.glides)
    implementation(libs.androidx.splash)
    kapt(libs.bundles.glides.kapt)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
}
