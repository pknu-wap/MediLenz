plugins {

    id("mediproject.android.feature")
    id("kotlinx-serialization")

}

android {
    namespace = "com.android.mediproject.core.data"

    buildFeatures {
        buildConfig = true
    }
}
hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.paging.runtime)
}