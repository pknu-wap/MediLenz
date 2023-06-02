package com.android.mediproject.core.model.local.navargs

import android.os.Bundle
import androidx.navigation.NavArgs
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.javaType

abstract class BaseNavArgs(
    val className: String) : NavArgs {
    fun toBundle(): Bundle {
        val result = Bundle()
        toMap().forEach { (key, value) ->
            when (value) {
                null -> return@forEach
                is String -> result.putString(key, value)
                is Int -> result.putInt(key, value)
                is Long -> result.putLong(key, value)
                is Float -> result.putFloat(key, value)
                is Boolean -> result.putBoolean(key, value)
            }
        }
        return result
    }

    fun toBundle(map: Map<String, Any?>): Bundle {
        val result = Bundle()
        map.forEach { (key, value) ->
            when (value) {
                null -> return@forEach
                is String -> result.putString(key, value)
                is Int -> result.putInt(key, value)
                is Long -> result.putLong(key, value)
                is Float -> result.putFloat(key, value)
                is Boolean -> result.putBoolean(key, value)
            }
        }
        return result
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        @JvmStatic
        fun fromBundle(bundle: Bundle): BaseNavArgs {
            if (bundle.containsKey("className") && bundle.size() == 1) return empty(bundle.getString("className")!!)

            val kClass: KClass<BaseNavArgs> = Class.forName(bundle.getString("className")!!).kotlin as KClass<BaseNavArgs>
            bundle.classLoader = kClass.java.classLoader

            val constructor = kClass.primaryConstructor!!
            val args = constructor.parameters.map { parameter ->
                val type = parameter.type.javaType
                with(type) {
                    when (type) {
                        String::class.java -> bundle.getString(parameter.name)
                        Int::class.java -> bundle.getInt(parameter.name)
                        Long::class.java -> bundle.getLong(parameter.name)
                        Float::class.java -> bundle.getFloat(parameter.name)
                        Boolean::class.java -> bundle.getBoolean(parameter.name)
                        else -> {}
                    }
                }
            }
            return constructor.call(*args.toTypedArray())
        }

        @JvmStatic
        private fun empty(className: String): BaseNavArgs {
            val kClass: KClass<BaseNavArgs> = Class.forName(className).kotlin as KClass<BaseNavArgs>

            val constructor = kClass.primaryConstructor!!

            val args: List<Any> = constructor.parameters.map { kProperty1 ->
                when (kProperty1.type) {
                    String::class.starProjectedType -> ""
                    Int::class.starProjectedType -> 0
                    Long::class.starProjectedType -> 0L
                    Float::class.starProjectedType -> 0f
                    Boolean::class.starProjectedType -> false
                    else -> false
                }
            }


            return constructor.call(*args.toTypedArray())
        }
    }


    fun toMap(): Map<String, Any?> = this::class.memberProperties.associate { property ->
        property.name to property.getter.call(this)
    }
}