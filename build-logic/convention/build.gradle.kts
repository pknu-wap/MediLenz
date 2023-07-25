plugins {
    `kotlin-dsl`
}

group = "com.android.mediproject.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
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
        register("androidFeatureCompose") {
            id = "mediproject.android.feature.compose"
            implementationClass = "ComposeFeatureConventionPlugin"
        }
        register("androidFeature") {
            id = "mediproject.android.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        register("kotlinJvm") {
            id = "mediproject.kotlin.jvm"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
