package com.android.mediproject.core.ai.tflite.classification

import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.core.model.ai.DetectionResultEntity
import kotlinx.coroutines.flow.Flow

interface MedicineClassifier {
    fun classify(entities: DetectionResultEntity): Flow<Result<List<ClassificationResultEntity>>>
}
