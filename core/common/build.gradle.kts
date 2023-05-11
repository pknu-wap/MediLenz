import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.feature")
    id("kotlinx-serialization")
}

android {
    namespace = "com.android.mediproject.core.common"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("/apikey.properties").bufferedReader())
        buildConfigField("String", "DATA_GO_KR_SERVICE_KEY", "\"${properties["dataGoKrServiceKey"]}\"")
        buildConfigField("String", "DATA_GO_KR_BASE_URL", "\"${properties["dataGoKrBaseUrl"]}\"")

    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.glides)
}