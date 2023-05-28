plugins {

    id("mediproject.android.feature")
    id("kotlinx-serialization")

}

android {
    namespace = "com.android.mediproject.core.network"

    buildFeatures {
        buildConfig = true
    }
}

hilt {
    enableAggregatingTask = true
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.bundles.retrofits)
    implementation(libs.kotlinx.datetime)
    implementation(libs.okhttp.logginginterceptor)
    implementation(libs.okhttp)
    kapt(libs.androidx.hilt.compilerKapt)
    kapt(libs.androidx.hilt.work.compilerKapt)
}