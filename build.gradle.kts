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

    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
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

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configurations {
        ktlint
        detekt
    }

    repositories {
        mavenCentral()
    }

    detekt {
        parallel = true
        buildUponDefaultConfig = true
        config.setFrom(files("$rootDir/detekt-config.yml"))
    }

    ktlint {
        debug.set(true)
        verbose.set(true)
    }
}
