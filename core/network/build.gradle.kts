plugins {
    id("mediproject.android.feature")
    id("kotlinx-serialization")
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.android.mediproject.core.network"

    hilt {
        enableAggregatingTask = true
    }

}



dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.bundles.retrofits)
    implementation(libs.bundles.dataStores)
    implementation(libs.google.protobuf.kotlin.lite)
    //implementation(libs.kotlinx.datetime)
    implementation(libs.okhttp.logginginterceptor)
    implementation(libs.okhttp)
    implementation(libs.jsoup)
    kapt(libs.androidx.hilt.compilerKapt)
    kapt(libs.androidx.hilt.work.compilerKapt)
}
