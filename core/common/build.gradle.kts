import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("mediproject.android.feature")
    id("kotlinx-serialization")
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
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
        buildConfigField("String", "AWS_USER_POOL", "\"${properties["AWS_USER_POOL"]}\"")
        buildConfigField("String", "AWS_USER_CLIENT_ID", "\"${properties["AWS_USER_CLIENT_ID"]}\"")
        buildConfigField("String", "AWS_USER_CLIENT_SECRET", "\"${properties["AWS_USER_CLIENT_SECRET"]}\"")
    }

    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(project(":core:model"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflection)
    implementation(libs.bundles.glides)
    implementation(libs.lottie)
    kapt(libs.bundles.glides.kapt)

    implementation(libs.androidx.paging.runtime)

    ksp(libs.ksealedbinding.compiler)
    implementation(libs.ksealedbinding.annotation)
}
