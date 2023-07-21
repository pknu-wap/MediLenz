package com.android.mediproject.feature.camera.tflite

import kotlinx.coroutines.flow.SharedFlow
import org.tensorflow.lite.task.gms.vision.detector.Detection


/**
 * AI를 제어하는 인터페이스
 */
interface AiController {
    val detectionResult: SharedFlow<List<Detection>>

}
