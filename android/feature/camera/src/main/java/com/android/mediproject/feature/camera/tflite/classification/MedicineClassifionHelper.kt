package com.android.mediproject.feature.camera.tflite.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.scale
import com.android.mediproject.core.model.ai.ClassificationRecognition
import com.android.mediproject.core.model.ai.ClassificationResult
import com.android.mediproject.core.model.ai.DetectionObjects
import com.android.mediproject.feature.camera.ml.MedicineModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineClassifionHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val targetSize = Size(224, 224)

    private val medicineModel: MedicineModel by lazy {
        val options = Model.Options.Builder().setDevice(Model.Device.NNAPI).build()
        MedicineModel.newInstance(context, options)
    }

    suspend fun analyze(images: DetectionObjects): List<ClassificationResult> {
        val items = images.detection.mapIndexed { index, detection ->
            val tfImage = TensorImage.fromBitmap(detection.image.resizeBitmap())
            val outputs = medicineModel.process(tfImage).scoresAsCategoryList.first()
            val maxConfidence = outputs.score
            val label = outputs.label

            ClassificationResult(detection, ClassificationRecognition(label, maxConfidence))
        }

        return items
    }

    private fun Bitmap.resizeBitmap() = scale(targetSize.width, targetSize.height)

}