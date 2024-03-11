import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("mediproject.android.library")
                apply("mediproject.android.hilt")
                apply("androidx.navigation.safeargs.kotlin")
            }

            dependencies {
                IMPLEMENTATION(libs.findBundle("navigations").get())
                IMPLEMENTATION(libs.findBundle("lifecycles").get())
                IMPLEMENTATION(libs.findBundle("kotlins").get())
                KAPT(libs.findLibrary("androidx.lifecycle.compilerKapt").get())
            }
        }
    }
}
