import com.android.build.gradle.LibraryExtension
import com.android.mediproject.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.android.library")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
            }

            dependencies {
                "testImplementation"(libs.findBundle("testLocal").get())
                "androidTestImplementation"(libs.findBundle("testAndroid").get())
            }
        }
    }
}
