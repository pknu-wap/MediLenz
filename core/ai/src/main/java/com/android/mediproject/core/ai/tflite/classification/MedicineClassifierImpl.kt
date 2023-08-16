package com.android.mediproject.core.ai.tflite.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.scale
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.onLoadFailed
import com.android.mediproject.core.ai.onLoaded
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.ai.ClassificationRecognitionEntity
import com.android.mediproject.core.model.ai.ClassificationResultEntity
import com.android.mediproject.core.model.ai.DetectionResultEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.IOException
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.exp


@OptIn(DelicateCoroutinesApi::class)
@Singleton
class MedicineClassifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : MedicineClassifier {
    private val targetSize = Size(224, 224)

    private var _aiModel: Module? = null
    private val aiModel get() = _aiModel!!

    private var _metaData: ClassificationMetaDataEntity? = null

    private val metaData get() = _metaData!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    private val aiModelState get() = _aiModelState.asStateFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    init {
        GlobalScope.launch(ioDispatcher) {
            _aiModelState.value = AiModelState.Loading
            try {
                _aiModel = LiteModuleLoader.loadModuleFromAsset(context.assets, "model21.ptl")
                _metaData = json
                    .decodeFromString(
                        ClassificationMetaDataEntity.serializer(),
                        context.assets.open("metadata.json").bufferedReader().use { it.readText() },
                    )
                _aiModelState.value = AiModelState.Loaded
            } catch (e: IOException) {
                _aiModelState.value = AiModelState.LoadFailed
            }
        }
    }

    override fun classify(entities: DetectionResultEntity): Flow<Result<List<ClassificationResultEntity>>> = channelFlow {
        aiModelState.collectLatest { state ->
            state.onLoaded {
                val result = Result.success(
                    entities.detection.map { detection ->
                        val inputTensor: Tensor = WeakReference(
                            TensorImageUtils.bitmapToFloat32Tensor(
                                detection.image.resizeBitmap(),
                                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB,
                            ),
                        ).get()!!
                        val outputTensor: Tensor = aiModel.forward(IValue.from(inputTensor)).toTensor()
                        val scores = softmax(outputTensor.dataAsFloatArray)
                        var maxScore = scores.withIndex().maxBy { it.value }
                        val itemSeq: String = metaData.getClass(maxScore.index)
                        ClassificationResultEntity(detection, ClassificationRecognitionEntity(itemSeq, maxScore.value))
                    },
                )
                send(result)
            }.onLoadFailed {
                send(Result.failure(Throwable("이미지 분류 모델 초기화 실패")))
            }
        }
    }

    private fun softmax(scores: FloatArray): FloatArray {
        val maxScore = scores.max()

        val expScores = FloatArray(scores.size) { i ->
            exp((scores[i] - maxScore).toDouble()).toFloat()
        }
        val sumExpScores = expScores.sum()

        return FloatArray(scores.size) { i -> expScores[i] / sumExpScores }
    }

    private fun Bitmap.resizeBitmap() = scale(targetSize.width, targetSize.height)

}
