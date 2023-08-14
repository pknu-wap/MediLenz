package com.android.mediproject.feature.camera.tflite


/**
 * AI를 제어하는 인터페이스
 */
interface AiController {
    suspend fun getObjectDetectorStatus(): Result<Unit>

}
