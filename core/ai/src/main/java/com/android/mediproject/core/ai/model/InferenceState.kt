package com.android.mediproject.core.ai.model

import io.github.pknujsp.core.annotation.KBindFunc


@KBindFunc
sealed interface InferenceState<out T> {
    object Initial : InferenceState<Nothing>
    data class Success<out T>(val entity: T, var consumed: Boolean = false) : InferenceState<T>
    object Failure : InferenceState<Nothing>
}
