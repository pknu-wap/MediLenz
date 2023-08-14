package com.android.mediproject.feature.camera.tflite

import com.android.mediproject.feature.camera.tflite.classification.MedicineClassifier
import com.android.mediproject.feature.camera.tflite.classification.MedicineClassifierImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TfLiteModule {
    @Provides
    fun providesCameraController(medicineClassifierImpl: MedicineClassifierImpl): MedicineClassifier = medicineClassifierImpl

}
