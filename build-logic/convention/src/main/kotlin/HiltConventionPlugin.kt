import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.kapt")
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                IMPLEMENTATION(libs.findBundle("daggerhilt").get())
                KAPT(libs.findLibrary("dagger.hilt.compiler").get())
                KAPT(libs.findLibrary("androidx.hilt.work.compiler").get())
                ANDROID_TEST_IMPLEMENTATION(libs.findLibrary("dagger.hilt.android.testing").get())
            }
        }
    }

}
