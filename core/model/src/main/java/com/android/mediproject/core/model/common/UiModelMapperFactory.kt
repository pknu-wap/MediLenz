package com.android.mediproject.core.model.common

import com.android.mediproject.core.model.servercommon.NetworkApiResponse
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class UiModelMapperFactory {
    companion object {
        var mappers: Map<KClass<out NetworkApiResponse.ListItem>, KClass<out UiModelMapper<out UiModel>>> = mapOf()

        fun register(wrapper: KClass<out UiModelMapper<out UiModel>>, source: KClass<out NetworkApiResponse.ListItem>) {
            mappers = mappers.plus(Pair(source, wrapper))
        }

        inline fun <reified OUT : UiModel> create(source: Any): UiModelMapper<OUT> =
            mappers[source::class]!!.primaryConstructor!!.call(source) as UiModelMapper<OUT>

    }
}
