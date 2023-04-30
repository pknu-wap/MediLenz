plugins {
    id("org.jetbrains.kotlin.android")
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    id("kotlinx-serialization")

}

android {
    namespace = "com.android.mediproject.core.data"
}
hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
}