package com.android.mediproject.core.ai.classification

import android.graphics.Bitmap
import com.android.mediproject.core.ai.AiModel
import com.android.mediproject.core.ai.model.ClassificationResultEntity
import kotlinx.coroutines.flow.Flow

interface MedicineClassifier : AiModel {
    fun classify(bitmaps: List<Bitmap>): Flow<Result<ClassificationResultEntity>>
}
