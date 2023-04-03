plugins {
    `kotlin-dsl`
}

group = "com.android.mediproject.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "mediproject.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "mediproject.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidApplication") {
            id = "mediproject.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidFeature") {
            id = "mediproject.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}