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
import com.android.mediproject.feature.camera.AiModelState
import com.android.mediproject.feature.camera.ml.MedicineModel
import com.android.mediproject.feature.camera.onLoadFailed
import com.android.mediproject.feature.camera.onLoaded
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class MedicineClassifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : MedicineClassifier {
    private val targetSize = Size(224, 224)

    private var _aiModel: MedicineModel? = null
    private val aiModel get() = _aiModel!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    private val aiModelState get() = _aiModelState.asStateFlow()

    init {
        GlobalScope.launch(ioDispatcher) {
            _aiModelState.value = AiModelState.Loading
            try {
                _aiModel = MedicineModel.newInstance(context, Model.Options.Builder().setDevice(Model.Device.NNAPI).build())
                _aiModelState.value = AiModelState.Loaded
            } catch (e: IOException) {
                _aiModelState.value = AiModelState.LoadFailed
            }
        }
    }

    override fun classify(entities: DetectionResultEntity): Flow<Result<List<ClassificationResultEntity>>> = channelFlow {
        aiModelState.collect { state ->
            state.onLoaded {
                send(
                    Result.success(
                        entities.detection.map { detection ->
                            val tfImage = TensorImage.fromBitmap(detection.image.resizeBitmap())
                            val outputs = aiModel.process(tfImage).scoresAsCategoryList.first()
                            val maxConfidence = outputs.score
                            val label = outputs.label

                            ClassificationResultEntity(detection, ClassificationRecognitionEntity(label, maxConfidence))
                        },
                    ),
                )
            }.onLoadFailed {
                send(Result.failure(Throwable("이미지 분류 모델 초기화 실패")))
            }
        }
    }


    private fun Bitmap.resizeBitmap() = scale(targetSize.width, targetSize.height)

}
