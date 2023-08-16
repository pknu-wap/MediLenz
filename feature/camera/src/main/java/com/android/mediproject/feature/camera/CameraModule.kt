package com.android.mediproject.feature.camera

import com.android.mediproject.core.ai.tflite.camera.AiController
import com.android.mediproject.core.ai.tflite.camera.CameraController
import com.android.mediproject.core.ai.tflite.camera.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Provides
    fun providesCameraController(): CameraController = CameraHelper.instance

    @Provides
    fun providesAiController(): AiController = CameraHelper.instance
}
