package com.android.mediproject

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {

    commonExtension.apply {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        @Suppress("UnstableApiUsage") buildFeatures {
            compose = true
        }

        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().toString().toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("kotlinCompilerExtension").get().toString()
            useLiveLiterals = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add("implementation", platform(bom))
        }
    }
}
