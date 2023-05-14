package com.android.mediproject.core.annotation

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.concurrent.Flow
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Flow.Processor::class)
class DeepLinkNavArgsProcessor : AbstractProcessor() {
    private lateinit var filer: Filer

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(DeepLinkNavArgs::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(DeepLinkNavArgs::class.java)
            .forEach { element ->
                if (element is TypeElement) {
                    val className = element.simpleName.toString()
                    val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
                    val newClassName = "Final$className"

                    // class 생성
                    val classBuilder = TypeSpec.classBuilder(newClassName)
                        .addModifiers(KModifier.PUBLIC, KModifier.FINAL).addSuperinterface(NavArgs::class)

                    // 모든 속성을 NonNull Val로 설정
                    element.enclosedElements.filter { it.kind.isField }.forEach { field ->
                        val fieldName = field.simpleName.toString()
                        val fieldType = (field as VariableElement).asType()

                        // NonNull 타입으로 바꾸기
                        val nonNullFieldType = fieldType.toString().removeSuffix("?")

                        val propertySpec = PropertySpec.builder(
                            fieldName,
                            ClassName.bestGuess(nonNullFieldType),
                            KModifier.PUBLIC, KModifier.FINAL
                        ).build()

                        classBuilder.addProperty(propertySpec)
                    }

                    // Java 파일 생성
                    val fileSpec = FileSpec.builder(packageName, newClassName)
                        .addType(classBuilder.build())
                        .build()

                    fileSpec.writeTo(filer)
                }
            }

        return true
    }
}