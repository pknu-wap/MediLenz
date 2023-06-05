package com.android.mediproject.feature.camera

import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import com.android.mediproject.feature.camera.tflite.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {

    @Provides
    @Singleton
    fun providersCameraController(
        cameraHelper: CameraHelper): CameraController = cameraHelper

    @Provides
    @Singleton
    fun providesAiController(
        cameraHelper: CameraHelper): AiController = cameraHelper

}