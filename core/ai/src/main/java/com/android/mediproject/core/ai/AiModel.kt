package com.android.mediproject.core.ai

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow

interface AiModel {
    suspend fun initialize(@ApplicationContext context: Context): Result<Unit>

    fun release()

    val aiModelState: StateFlow<AiModelState>
}
