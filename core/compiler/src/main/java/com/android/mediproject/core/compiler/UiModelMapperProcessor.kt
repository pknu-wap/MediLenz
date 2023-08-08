package com.android.mediproject.core.compiler

import com.android.mediproject.core.annotation.UiModelMapping
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class UiModelMapperProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>,
) : SymbolProcessor {
    private companion object {
        val ANNOTATION_TYPE: String = UiModelMapping::class.java.canonicalName
        val OUTPUT_FILE_NAME = "${ANNOTATION_TYPE.split(".").last()}Register"
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val declarations = resolver.getSymbolsWithAnnotation(ANNOTATION_TYPE).filterIsInstance<KSClassDeclaration>().toList()
        createFile(declarations)
        return declarations
    }

    private fun createFile(declarations: List<KSClassDeclaration>) {
        val uiModelMapperFactoryClass = ClassName("com.android.mediproject.core.model.common", "UiModelMapperFactory")

        val newFileSpec = FileSpec.builder(uiModelMapperFactoryClass.packageName, OUTPUT_FILE_NAME).addType(
            TypeSpec.objectBuilder(OUTPUT_FILE_NAME).apply {
                val func = FunSpec.builder("register")

                declarations.forEach { declaration ->
                    val properties = declaration.getDeclaredProperties().toList()
                    val sourceType = properties.first().type.resolve().declaration

                    func.addStatement(
                        "UiModelMapperFactory.register(${declaration.qualifiedName!!.asString()}::class, ${sourceType.qualifiedName!!.asString()}::class)",
                    )
                }
                addFunction(func.build())
            }.build(),
        ).build()

        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies(false, *declarations.map { it.containingFile!! }.toTypedArray()),
                packageName = uiModelMapperFactoryClass.packageName,
                fileName = OUTPUT_FILE_NAME,
            ).bufferedWriter().use {
                newFileSpec.writeTo(it)
            }
        } catch (e: Exception) {
        }
    }
}


/*
@com.android.mediproject.core.annotation.UiModelMapper
class AdminActionListUiModelMapper(override val source: AdminActionListResponse.Item) : UiModelMapper<AdminAction>() {
    private companion object {
        init {
            UiModelMapperFactory.register(AdminActionListUiModelMapper::class, AdminActionListResponse.Item::class)
        }
    }

    override fun convert(): AdminAction {
        return source.run {
            AdminAction(
                companyAddress = companyAddress,
                adminAction = adminAction,
                adminActionNo = adminActionNo,
                violationLaw = violationLaw,
                companyBizrNo = companyBizrNo ?: "",
                companyName = companyName,
                companyRegistrationNumber = companyRegistrationNumber,
                violationDetails = violationDetails,
                itemName = itemName ?: "",
                itemSeq = itemSeq ?: "",
                adminActionDate = adminActionDate.toLocalDate("yyyyMMdd"),
                disclosureEndDate = disclosureEndDate.toLocalDate("yyyyMMdd"),
            )
        }
    }
}
 */
