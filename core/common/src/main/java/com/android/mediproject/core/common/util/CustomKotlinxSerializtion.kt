package com.android.mediproject.core.common.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.annotations.NotNull
import javax.annotation.Nonnull

/**
 * @Serializable 어노테이션이 붙은 클래스에 대해 Json으로 인코딩 합니다.
 */
inline fun <reified T : Any> T.encodeToJsonString(): String {
    if (this::class.annotations.any { it.annotationClass == Serializable::class }) {
        return T::class.java.let {
            Json.encodeToString(it)
        }
    } else {
        throw UnsupportedOperationException("Extension function can be applied only on classes annotated with @Serializable!!!")
    }
}

/**
 * @Serializable 어노테이션이 붙은 클래스에 대해서만 Json String을 디코딩 합니다.
 */
inline fun <reified T> String.decodeFromjsonString(@NotNull @Nonnull c: T): T {
    if (c!!::class.annotations.any { it.annotationClass == Serializable::class }) {
        return Json.decodeFromString<T>(this)
    } else {
        throw UnsupportedOperationException("Extension function can be applied only on classes annotated with @Serializable!!!")
    }
}