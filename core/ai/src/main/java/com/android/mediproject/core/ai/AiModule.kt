package com.android.mediproject.core.ai

import com.android.mediproject.core.ai.classification.MedicineClassifier
import com.android.mediproject.core.ai.classification.MedicineClassifierImpl
import com.android.mediproject.core.ai.detection.MedicineDetector
import com.android.mediproject.core.ai.detection.MedicineDetectorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun providesMedicineClassifier(): MedicineClassifier = MedicineClassifierImpl

    @Provides
    @Singleton
    @Named(AiModelManager.Classifier)
    fun providesMedicineClassifierModelManager(): AiModel = MedicineClassifierImpl


    @Provides
    @Singleton
    fun providesMedicineDetector(): MedicineDetector = MedicineDetectorImpl


    @Provides
    @Singleton
    @Named(AiModelManager.Detector)
    fun providesMedicineDetectorModelManager(): AiModel = MedicineDetectorImpl
}

class AiModelManager {
    companion object {
        const val Detector: String = "Detector"
        const val Classifier: String = "Classifier"
    }
}
