package com.android.mediproject.core.ai

import io.github.pknujsp.core.annotation.KBindFunc

@KBindFunc
sealed interface AiModelState {
    object Initial : AiModelState
    object Loading : AiModelState
    object Loaded : AiModelState
    object LoadFailed : AiModelState
}
