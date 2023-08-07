package com.android.mediproject.core.model.common

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class UiModelWrapperFactory {
    companion object {
        val wrappers: Map<KClass<*>, KClass<UiModelWrapper<out UiModel>>> = mapOf()

        inline fun <reified SRC : Any> register(wrapper: KClass<out UiModelWrapper<out UiModel>>, source: KClass<SRC>) {
            wrappers.plus(Pair(source, wrapper))
        }

        inline fun <reified OUT : UiModel> create(source: Any): UiModelWrapper<OUT> =
            wrappers[source::class]!!.primaryConstructor!!.call(source) as UiModelWrapper<OUT>

    }
}
