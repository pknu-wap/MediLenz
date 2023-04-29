plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.nav.safeargs.kotlin) apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin apply false
}