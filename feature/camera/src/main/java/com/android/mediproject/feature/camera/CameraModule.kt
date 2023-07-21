package com.android.mediproject.feature.camera

import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import com.android.mediproject.feature.camera.tflite.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Provides
    fun providesCameraController(cameraHelper: CameraHelper): CameraController = CameraHelper.instance

    @Provides
    fun providesAiController(cameraHelper: CameraHelper): AiController = CameraHelper.instance
}
