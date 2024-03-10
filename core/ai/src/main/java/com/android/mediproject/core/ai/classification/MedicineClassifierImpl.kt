package com.android.mediproject.core.ai.classification

import android.content.Context
import android.graphics.Bitmap
import com.android.mediproject.core.ai.AiModelState
import com.android.mediproject.core.ai.model.ClassificationResultEntity
import com.android.mediproject.core.ai.onLoadFailed
import com.android.mediproject.core.ai.onLoaded
import com.android.mediproject.core.ai.util.ObjectBitmapCreator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference


@OptIn(DelicateCoroutinesApi::class)
object MedicineClassifierImpl : MedicineClassifier {
    private var _aiModels: List<Module>? = null
    private val aiModels get() = _aiModels!!

    private var _metaData: List<ClassificationMetaDataEntity>? = null

    private val metaData get() = _metaData!!

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.Initial)
    override val aiModelState get() = _aiModelState.asStateFlow()

    private val objectBitmapCreator = ObjectBitmapCreator

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
    }

    override fun classify(bitmaps: List<Bitmap>): Flow<Result<ClassificationResultEntity>> = channelFlow {
        aiModelState.collectLatest { state ->
            state.onLoaded {
                val result = bitmaps.map { bitmap ->
                    val inputTensor: Tensor = WeakReference(
                        TensorImageUtils.bitmapToFloat32Tensor(
                            objectBitmapCreator.resizeBitmap(bitmap),
                            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB,
                        ),
                    ).get()!!
                    val input = WeakReference(IValue.from(inputTensor)).get()!!

                    val inferenced = aiModels.zip(
                        metaData,
                    ).map { (model, metaData) ->
                        val outputTensor: Tensor = model.forward(input).toTensor()
                        val scores = outputTensor.dataAsFloatArray
                        val maxScore = scores.withIndex().sortedByDescending { it.value }
                        metaData.getClass(maxScore.first().index) to maxScore.first().value
                    }

                    val maxScore = inferenced.sortedByDescending { it.second }
                    ClassificationResultEntity.Item(maxScore.first().first, maxScore.first().second)
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
            val files = ClassificationModelInfo.files.map { (modelFileName, metaDataFileName) ->
                val modelFilePath = "${context.filesDir.absolutePath}/$modelFileName"
                val outputFile = File(modelFilePath)

                context.assets.open(modelFileName).use { input ->
                    outputFile.outputStream().use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var read: Int

                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                        }
                        output.flush()
                    }
                }

                Module.load(modelFilePath) to json.decodeFromString(
                    ClassificationMetaDataEntity.serializer(),
                    context.assets.open(metaDataFileName).bufferedReader().use { it.readText() },
                )
            }

            files.run {
                _aiModels = map { (model, _) -> model }
                _metaData = map { (_, metaData) -> metaData }
            }

            //_aiModel = LiteModuleLoader.loadModuleFromAsset(context.assets, "regnetxepoch22.pth")

            _aiModelState.value = AiModelState.Loaded
            Result.success(Unit)
        } catch (e: IOException) {
            _aiModelState.value = AiModelState.LoadFailed
            Result.failure(e)
        }
    }

    override fun release() {

    }

}
