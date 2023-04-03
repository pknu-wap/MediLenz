import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("hilt.android").get())
                add("testImplementation", libs.findLibrary("hilt.android.testing").get())
                add("implementation", libs.findLibrary("hilt.navigation.fragment").get())
                add("implementation", libs.findLibrary("hilt.work").get())

                add("kapt", libs.findLibrary("hilt.work.compiler").get())
                add("kapt", libs.findLibrary("hilt.compiler").get())
            }

        }
    }

}