import com.android.build.gradle.LibraryExtension
import com.android.mediproject.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }
            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
    }
}