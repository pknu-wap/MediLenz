plugins {
    id("mediproject.android.library")
    id("mediproject.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.android.mediproject.core.datastore"

    buildFeatures {
        buildConfig = true
    }
}

protobuf {
    protoc {
        artifact = libs.google.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.bundles.dataStores)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.google.protobuf.kotlin.lite)
}