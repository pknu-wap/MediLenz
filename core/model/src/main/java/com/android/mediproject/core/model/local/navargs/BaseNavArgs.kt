package com.android.mediproject.core.model.local.navargs

import android.os.Bundle
import androidx.navigation.NavArgs
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

abstract class BaseNavArgs : NavArgs {
    @Suppress("CAST_NEVER_SUCCEEDS")
    fun toBundle(): Bundle {
        val result = Bundle()
        toMap().forEach { (key, value) ->
            result.putString(key, value)
        }
        return result
    }

    companion object {
        @JvmStatic
        fun fromBundle(bundle: Bundle): BaseNavArgs {
            val kClass = this::class.class
            bundle.classLoader = kClass.java.classLoader

            val constructor = kClass.primaryConstructor!!
            val args = constructor.parameters.map { parameter ->
                bundle.getString(parameter.name, "")
            }
            return constructor.call(*args.toTypedArray())
        }
    }


    fun toMap(): Map<String, String> = this::class.memberProperties.let { properties ->
        properties.associate { property ->
            property.name to property.getter.call(this).toString()
        }
    }

}