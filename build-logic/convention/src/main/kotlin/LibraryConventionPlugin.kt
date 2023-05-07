import com.android.build.gradle.LibraryExtension
import com.android.mediproject.configureAndroidCompose
import com.android.mediproject.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
            }

            //val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // "testImplementation"(kotlin("test"))
                // "androidTestImplementation"(kotlin("test"))
            }
        }
    }
}