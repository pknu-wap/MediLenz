plugins {
    id("kotlin")
    id(libs.plugins.kapt.get().pluginId)
}

// jvmTarget = 17
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.google.autoservice)
    kapt(libs.google.autoservice)
    implementation(libs.google.autoservice.annotation)
    implementation(libs.kotlin.reflection)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.poet)
}