package com.android.mediproject.core.ai.tflite.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner


/**
 * 카메라를 제어하는 인터페이스
 */
interface CameraController {
    suspend fun connectCamera(previewView: PreviewView): Result<Unit>


    var detectionCallback: CameraHelper.ObjDetectionCallback

    var fragmentLifeCycleOwner: LifecycleOwner
}
