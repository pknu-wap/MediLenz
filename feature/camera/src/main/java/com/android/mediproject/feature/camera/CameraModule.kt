package com.android.mediproject.feature.camera

import android.content.Context
import com.android.mediproject.feature.camera.tflite.AiController
import com.android.mediproject.feature.camera.tflite.CameraController
import com.android.mediproject.feature.camera.tflite.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class CameraModule {
    @Provides
    @ViewModelScoped
    fun providesCameraHelper(@ApplicationContext context: Context): CameraHelper = CameraHelper(context)

    @Provides
    fun providesCameraController(cameraHelper: CameraHelper): CameraController = cameraHelper

    @Provides
    fun providesAiController(cameraHelper: CameraHelper): AiController = cameraHelper

}