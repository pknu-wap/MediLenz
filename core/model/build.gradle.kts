plugins {
    id("kotlinx-serialization")
    id("kotlin")
}



dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}