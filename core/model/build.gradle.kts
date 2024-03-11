plugins {
    id("mediproject.android.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nav.safeargs.kotlin)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.android.mediproject.core.model"

    buildTypes {
        getByName("debug") {
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/debug/kotlin"))
                }
            }
        }
    }
}



dependencies {
    ksp(project(":core:compiler"))
    ksp(libs.ksealedbinding.compiler)
    implementation(project(":core:annotation"))
    implementation(libs.ksealedbinding.annotation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.reflection)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.tensorflow.lite.taskVisionPlayServices)
}
