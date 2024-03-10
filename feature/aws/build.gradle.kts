plugins {
    id("mediproject.android.feature")
}

android {
    namespace = "com.android.mediproject.feature.aws"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.bundles.uiAndroidx)
    implementation(libs.aws.android.sdk.cognito)
}
