package com.android.mediproject.core.model.local.navargs

import androidx.navigation.NavArgs
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

abstract class BaseNavArgs : NavArgs {

    companion object {

        @JvmStatic
        fun fromBundle(bundle: android.os.Bundle): BaseNavArgs {
            bundle.classLoader = BaseNavArgs::class.java.classLoader
            val thisClass = BaseNavArgs::class
            
            return thisClass.memberProperties.map {
                it.name
            }.map { propertyName ->
                if (!bundle.containsKey(propertyName)) {
                    throw IllegalArgumentException("$propertyName 를 찾을 수 없습니다.")
                } else {
                    bundle.getString(propertyName)
                }
            }.toTypedArray().let {
                thisClass.primaryConstructor!!.call(*it)
            }
        }
    }
}