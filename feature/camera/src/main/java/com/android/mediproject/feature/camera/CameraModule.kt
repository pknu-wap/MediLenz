package com.android.mediproject.feature.camera

import com.android.mediproject.core.ai.camera.CameraController
import com.android.mediproject.core.ai.camera.CameraHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {


    @Provides
    fun providesCameraController(cameraHelper: CameraHelper): CameraController = cameraHelper

}
