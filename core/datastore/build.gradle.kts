plugins {

    id("mediproject.android.library")
    id("mediproject.android.hilt")

}

android {
    namespace = "com.android.mediproject.core.datastore"

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

    implementation(libs.bundles.dataStores)
    implementation(libs.kotlinx.coroutines.android)

}