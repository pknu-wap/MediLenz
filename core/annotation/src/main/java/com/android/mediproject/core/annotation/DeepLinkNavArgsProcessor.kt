package com.android.mediproject.core.annotation

// Annotation Processor Module
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class DeepLinkNavArgsProcessor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var elementUtils: Elements

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        elementUtils = processingEnv.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(DeepLinkNavArgs::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        // 여기에서 어노테이션을 붙인 클래스를 찾아서 새로운 클래스를 생성하는 로직이 필요합니다.
        // JavaPoet 라이브러리를 사용하여 새 클래스를 생성할 수 있습니다.
        // 이 예제에서는 로직을 생략하였습니다.
        return true
    }
}