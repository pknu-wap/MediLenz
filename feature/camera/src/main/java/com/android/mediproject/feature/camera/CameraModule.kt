package com.android.mediproject.feature.camera

import android.content.Context
import com.android.mediproject.core.ai.camera.CameraController
import com.android.mediproject.core.ai.camera.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    @Provides
    @Singleton
    fun providesCameraHelper(@ApplicationContext context: Context): CameraHelper = CameraHelper(context)

    @Provides
    fun providesCameraController(cameraHelper: CameraHelper): CameraController = cameraHelper

    @Provides
    fun providesAiController(cameraHelper: CameraHelper): AiController = cameraHelper
}
