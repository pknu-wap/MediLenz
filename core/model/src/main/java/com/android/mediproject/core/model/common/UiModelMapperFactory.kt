package com.android.mediproject.core.model.common

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class UiModelMapperFactory {
    companion object {
        var mappers: Map<KClass<out Any>, KClass<out UiModelMapper<out UiModel>>> = mapOf()

        fun register(wrapper: KClass<out UiModelMapper<out UiModel>>, source: KClass<out Any>) {
            mappers = mappers.plus(Pair(source, wrapper))
        }

        inline fun <reified OUT : UiModel> create(source: Any): UiModelMapper<OUT> =
            mappers[source::class]!!.primaryConstructor!!.call(source) as UiModelMapper<OUT>

    }
}
