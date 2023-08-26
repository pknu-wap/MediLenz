plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.android.mediproject.core.database"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.rooms)
    implementation(libs.lz4)
    implementation(libs.kotlinx.coroutines.android)
    ksp(libs.androidx.room.compileKsp)
}
