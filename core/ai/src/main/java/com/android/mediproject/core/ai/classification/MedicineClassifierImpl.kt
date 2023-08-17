package com.android.mediproject.core.ai.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.core.graphics.scale
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.model.ClassificationResultEntity
import com.android.mediproject.core.ai.onLoadFailed
import com.android.mediproject.core.ai.onLoaded
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.math.exp


@OptIn(DelicateCoroutinesApi::class)
object MedicineClassifierImpl : MedicineClassifier {
    private val targetSize = Size(224, 224)

    private var _aiModel: Module? = null
    private val aiModel get() = _aiModel!!

    private var _metaData: ClassificationMetaDataEntity? = null

    private val metaData get() = _metaData!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    override val aiModelState get() = _aiModelState.asStateFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override fun classify(bitmaps: List<Bitmap>): Flow<Result<ClassificationResultEntity>> = channelFlow {
        aiModelState.collectLatest { state ->
            state.onLoaded {
                val result = bitmaps.map { bitmap ->
                    val inputTensor: Tensor = WeakReference(
                        TensorImageUtils.bitmapToFloat32Tensor(
                            bitmap.resizeBitmap(),
                            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB,
                        ),
                    ).get()!!
                    val outputTensor: Tensor = aiModel.forward(IValue.from(inputTensor)).toTensor()
                    val scores = softmax(outputTensor.dataAsFloatArray)
                    val maxScore = scores.withIndex().maxBy { it.value }
                    val itemSeq: String = metaData.getClass(maxScore.index)

                    ClassificationResultEntity.Item(itemSeq, maxScore.value)
                }
                send(Result.success(ClassificationResultEntity(result)))
            }.onLoadFailed {
                send(Result.failure(Throwable("이미지 분류 모델 초기화 실패")))
            }
        }
    }

    override suspend fun initialize(context: Context): Result<Unit> {
        _aiModelState.value = AiModelState.Loading
        return try {
            _aiModel = LiteModuleLoader.loadModuleFromAsset(context.assets, "model25.ptl")
            _metaData = json.decodeFromString(
                ClassificationMetaDataEntity.serializer(),
                context.assets.open("metadata.json").bufferedReader().use { it.readText() },
            )
            _aiModelState.value = AiModelState.Loaded
            Result.success(Unit)
        } catch (e: IOException) {
            _aiModelState.value = AiModelState.LoadFailed
            Result.failure(e)
        }
    }

    override fun release() {

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
