plugins {
    id("kotlin")
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    implementation(libs.google.autoservice)
    implementation(libs.google.autoservice.annotation)
    implementation(libs.kotlin.reflection)
    implementation(libs.kotlin.stdlib.root)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.poet)
    implementation(project(":core:model"))
}