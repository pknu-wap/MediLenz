import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType


internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

private var _targetSdk: Int? = null
private var _compileSdk: Int? = null
private var _minSdk: Int? = null

val VersionCatalog.targetSdk: Int
    get() = _targetSdk ?: findVersion("targetSdk").get().toString().toInt().apply {
        _targetSdk = this
    }

val VersionCatalog.compileSdk: Int
    get() = _compileSdk ?: findVersion("compileSdk").get().toString().toInt().apply {
        _compileSdk = this
    }

val VersionCatalog.minSdk: Int
    get() = _minSdk ?: findVersion("minSdk").get().toString().toInt().apply {
        _minSdk = this
    }


object SharedProperty {
    val jvmTarget: String = JavaVersion.VERSION_17.toString()
    val javaCompatibility: JavaVersion = JavaVersion.VERSION_17
}
