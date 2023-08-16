package com.android.mediproject.core.ai.tflite

import com.android.mediproject.core.ai.tflite.classification.MedicineClassifier
import com.android.mediproject.core.ai.tflite.classification.MedicineClassifierImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AiModule {
    @Provides
    fun providesMedicineClassifier(medicineClassifierImpl: MedicineClassifierImpl): MedicineClassifier = medicineClassifierImpl

}
