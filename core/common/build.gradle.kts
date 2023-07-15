import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("mediproject.android.feature")
    id("kotlinx-serialization")
    id(libs.plugins.kapt.get().pluginId)
}

android {
    namespace = "com.android.mediproject.core.common"

    @Suppress("UnstableApiUsage") buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("/apikey.properties").bufferedReader())
        buildConfigField("String", "DATA_GO_KR_SERVICE_KEY", "\"${properties["dataGoKrServiceKey"]}\"")
        buildConfigField("String", "DATA_GO_KR_BASE_URL", "\"${properties["dataGoKrBaseUrl"]}\"")
        buildConfigField("String", "AWS_BASE_URL", "\"${properties["awsUrl"]}\"")
        buildConfigField("String", "VERTEX_ENDPOINT_URL", "\"${properties["vertexEndpointUrl"]}\"")
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":core:model"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.glides)
    implementation(libs.lottie)
    kapt(libs.bundles.glides.kapt)
    kapt(libs.androidx.hilt.compilerKapt)
    implementation(libs.androidx.paging.runtime)
}