package com.android.mediproject.core.model.navargs

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavArgs
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

abstract class BaseNavArgs(
    val className: String,
) : NavArgs {
    fun toBundle(): Bundle {
        return bundleOf(*toMap().toList().toTypedArray())
    }

    fun toBundle(map: Map<String, Any>): Bundle {
        return bundleOf(*map.toList().toTypedArray())
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun fromBundle(bundle: Bundle): BaseNavArgs {
            if (bundle.containsKey("className") && bundle.size() == 1) return empty(bundle.getString("className")!!)

            val kClass: KClass<BaseNavArgs> = Class.forName(bundle.getString("className")!!).kotlin as KClass<BaseNavArgs>
            bundle.classLoader = kClass.java.classLoader

            val constructor = kClass.primaryConstructor!!
            val args = constructor.parameters.filter {
                it.name!! in bundle.keySet()
            }.map { parameter ->
                val value = bundle.get(parameter.name)!!
                val typeInBundle = value::class.starProjectedType

                if (typeInBundle == parameter.type) {
                    value
                } else {
                    convert(typeInBundle, parameter.type, value)
                }
            }

            return constructor.call(*args.toTypedArray())
        }

        @Suppress("UNCHECKED_CAST")
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

        @JvmStatic
        // NULL로 인한 오류를 막기 위해 기본값을 넣어준다.
        private fun emptyValue(type: KType) = when (type) {
            String::class.starProjectedType -> ""
            Int::class.starProjectedType -> 0
            Long::class.starProjectedType -> 0L
            Float::class.starProjectedType -> 0f
            Boolean::class.starProjectedType -> false
            else -> false
        }

        @JvmStatic
        private fun convert(fromType: KType, toType: KType, value: Any): Any {
            return when (fromType) {
                String::class.starProjectedType -> {
                    when (toType) {
                        Int::class.starProjectedType -> value.toString().toInt()
                        Long::class.starProjectedType -> value.toString().toLong()
                        Float::class.starProjectedType -> value.toString().toFloat()
                        Boolean::class.starProjectedType -> value.toString().toBoolean()
                        else -> value
                    }
                }

                Int::class.starProjectedType -> {
                    when (toType) {
                        String::class.starProjectedType -> value.toString()
                        Long::class.starProjectedType -> value.toString().toLong()
                        Float::class.starProjectedType -> value.toString().toFloat()
                        Boolean::class.starProjectedType -> value.toString().toBoolean()
                        else -> value
                    }
                }

                Long::class.starProjectedType -> {
                    when (toType) {
                        String::class.starProjectedType -> value.toString()
                        Int::class.starProjectedType -> value.toString().toInt()
                        Float::class.starProjectedType -> value.toString().toFloat()
                        Boolean::class.starProjectedType -> value.toString().toBoolean()
                        else -> value
                    }
                }

                Float::class.starProjectedType -> {
                    when (toType) {
                        String::class.starProjectedType -> value.toString()
                        Int::class.starProjectedType -> value.toString().toInt()
                        Long::class.starProjectedType -> value.toString().toLong()
                        Boolean::class.starProjectedType -> value.toString().toBoolean()
                        else -> value
                    }
                }

                Boolean::class.starProjectedType -> {
                    when (toType) {
                        String::class.starProjectedType -> value.toString()
                        Int::class.starProjectedType -> value.toString().toInt()
                        Long::class.starProjectedType -> value.toString().toLong()
                        Float::class.starProjectedType -> value.toString().toFloat()
                        else -> value
                    }
                }

                else -> value
            }
        }
    }


    // 속성 값이 NULL이면 빈 값을 넣어준다.
    fun toMap(): Map<String, Any> = this::class.memberProperties.associate { property ->
        property.name to (property.getter.call(this).run {
            if (this is String) replace("%", "%25") else this
        } ?: emptyValue(property.returnType))
    }
}
