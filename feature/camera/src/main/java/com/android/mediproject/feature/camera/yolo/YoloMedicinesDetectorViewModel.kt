package com.android.mediproject.feature.camera.yolo
/*
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.SurfaceHolder
import androidx.lifecycle.viewModelScope
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.common.viewmodel.UiState
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.camera.aimodel.DetectedObject
import com.android.mediproject.feature.camera.aimodel.Yolo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@HiltViewModel
class MedicinesDetectorViewModel @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private companion object {
        // YOLO 인터페이스
        private var yolo: Yolo? = null
    }

    /**
     * 검출된 객체 목록
     */
    private val _detactedObjects = MutableStateFlow<UiState<List<DetectedObject>>>(UiState.Initial)
    val detectedObjects get() = _detactedObjects.asStateFlow()

    /**
     * 객체를 검출하기 위해 쓰인 이미지
     */
    private val _capturedImage = MutableStateFlow<UiState<Bitmap>>(UiState.Initial)
    val capturedImage = _capturedImage.asStateFlow()

    val capturedImageValue
        get() = when (val state = capturedImage.value) {
            is UiState.Success -> state.data
            else -> null
        }

    private val _aiModelState = MutableStateFlow<AiModelState>(AiModelState.NotLoaded)
    val aiModelState get() = _aiModelState.asStateFlow()

    /**
     * Yolo 모델을 로드한다.
     */
    fun loadModel(assetManager: AssetManager) {
        viewModelScope.launch(ioDispatcher) {
            if (_aiModelState.value != AiModelState.Loaded) {
                _aiModelState.emit(AiModelState.Loading)
                yolo = Yolo()
                val loadResult = yolo?.loadModel(assetManager)

                if (loadResult == null) _aiModelState.emit(AiModelState.LoadFailed)
                else if (loadResult) _aiModelState.emit(AiModelState.Loaded)
                else _aiModelState.emit(AiModelState.LoadFailed)
            }
        }
    }

    /**
     * 검출된 객체를 가져오고 비트맵으로 변환한다.
     *
     * 이미지와 검출 객체 정보를 가져온 직후 카메라를 닫는다.
     *
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun capture() {
        viewModelScope.launch(ioDispatcher) {
            if (aiModelState.value == AiModelState.Loaded) {
                _detactedObjects.value = UiState.Loading
                yolo?.also { yolo ->
                    val img = yolo.getCurrentImage()
                    val objects = yolo.detectedObjects()
                    yolo.closeCamera()

                    val capturedBitmap = toBitmap(img.base64, img.width, img.height)
                    capturedBitmap.fold(onSuccess = { _capturedImage.emit(UiState.Success(it)) }, onFailure = {
                        // bitmap 변환 중 오류
                        _capturedImage.value = UiState.Error("converting error")
                        return@launch
                    })

                    if (objects == null) {
                        _detactedObjects.value = UiState.Error("no object detected")
                    } else {

                        if (objects.isEmpty()) {
                            _detactedObjects.value = UiState.Error("no object detected")
                            return@launch
                        }

                        // 검출된 객체의 base64를 bitmap으로 변환
                        objects.forEach { detectedObject ->
                            toBitmap(detectedObject.base64, detectedObject.width, detectedObject.height).fold(onSuccess = {
                                detectedObject.bitmap = it
                            }, onFailure = {
                                // bitmap 변환 중 오류
                                _detactedObjects.value = UiState.Error("converting error")
                                return@launch
                            })
                        }
                        // 검출된 객체를 저장
                        _detactedObjects.value = UiState.Success(objects.toList())
                    }
                }
            }
        }
    }

    fun surfaceCreated(holder: SurfaceHolder) {
        viewModelScope.launch(ioDispatcher) { yolo?.setOutputWindow(holder.surface) }
    }

    fun openCamera() {
        viewModelScope.launch(ioDispatcher) { yolo?.openCamera(1) }
    }

    fun closeCamera() {
        viewModelScope.launch(ioDispatcher) { yolo?.closeCamera() }
    }

    /**
     * base64 문자열을 비트맵으로 변환한다.
     *
     * @param base64 base64 문자열
     */
    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun toBitmap(base64: String, width: Int, height: Int): Result<Bitmap> =
        WeakReference(Base64.decode(base64.subSequence(0, base64.length), 0, base64.length)).get()?.let { decodedBytes ->
            Result.success(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, BitmapFactory.Options().apply {
                inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888
                outWidth = width
                outHeight = height
            }))
        } ?: Result.failure(Exception("Failed to decode base64 string"))

    /**
     * 카메라를 닫고, 검출된 객체와 이미지를 초기화한다.
     */
    fun clear() {
        yolo?.closeCamera()
    }
}

sealed class AiModelState {

    object NotLoaded : AiModelState()
    object Loading : AiModelState()
    object Loaded : AiModelState()
    object LoadFailed : AiModelState()
}

 */