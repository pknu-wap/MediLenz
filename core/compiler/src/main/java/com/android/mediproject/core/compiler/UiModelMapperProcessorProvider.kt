package com.android.mediproject.core.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class UiModelMapperProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = UiModelMapperProcessor(
        codeGenerator = environment.codeGenerator,
        logger = environment.logger,
        options = environment.options,
    )
}
