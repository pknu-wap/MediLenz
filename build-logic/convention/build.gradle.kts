plugins {
    `kotlin-dsl`
}

group = "com.android.mediproject.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "mediproject.android.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mediproject.android.library.compose"
            implementationClass = "ComposeLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "mediproject.android.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("androidApplication") {
            id = "mediproject.android.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "mediproject.android.application.compose"
            implementationClass = "ComposeApplicationConventionPlugin"
        }
        register("androidFeature") {
            id = "mediproject.android.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}