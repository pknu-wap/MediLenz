package com.android.mediproject.feature.camera.tflite.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.scale
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.ai.ClassificationRecognitionEntity
import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.core.model.ai.DetectionResultEntity
import com.android.mediproject.feature.camera.ml.MedicineModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class MedicineClassifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : MedicineClassifier {
    private val targetSize = Size(224, 224)
    private var _medicineModel: MedicineModel? = null
    private val medicineModel get() = _medicineModel!!

    init {
        GlobalScope.launch(ioDispatcher) {
            val options = Model.Options.Builder().setDevice(Model.Device.NNAPI).build()
            _medicineModel = MedicineModel.newInstance(context, options)
        }
    }

    override fun analyze(entities: DetectionResultEntity): List<ClassificationResultEntity> {
        val items = entities.detection.map { detection ->
            val tfImage = TensorImage.fromBitmap(detection.image.resizeBitmap())
            val outputs = medicineModel.process(tfImage).scoresAsCategoryList.first()
            val maxConfidence = outputs.score
            val label = outputs.label

            ClassificationResultEntity(detection, ClassificationRecognitionEntity(label, maxConfidence))
        }

        return items
    }

    private fun Bitmap.resizeBitmap() = scale(targetSize.width, targetSize.height)

}
