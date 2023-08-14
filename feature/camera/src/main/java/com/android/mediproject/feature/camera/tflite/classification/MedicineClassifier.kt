package com.android.mediproject.feature.camera.tflite.classification

import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.core.model.ai.DetectionResultEntity

interface MedicineClassifier {
    fun analyze(entities: DetectionResultEntity): List<ClassificationResultEntity>
}
