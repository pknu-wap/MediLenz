package com.android.mediproject.feature.camera

import io.github.pknujsp.core.annotation.KBindFunc

@KBindFunc
sealed interface AiModelState {
    object Initial : AiModelState
    object Loading : AiModelState
    object Loaded : AiModelState
    object LoadFailed : AiModelState
}
