import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath(libs.google.oss.licenses.plugin) {
            exclude(group = "com.google.protobuf")
        }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.nav.safeargs.kotlin) apply false
    alias(libs.plugins.kapt) apply false
    // alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}


gradle.allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs = options.compilerArgs + "-Xmaxerrs" + "1000"
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            //languageVersion = "2.0"
        }
    }
}