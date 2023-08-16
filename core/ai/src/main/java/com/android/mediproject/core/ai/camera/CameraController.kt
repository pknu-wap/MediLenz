package com.android.mediproject.core.ai.camera

import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.android.mediproject.core.ai.AiModelState
import kotlinx.coroutines.flow.StateFlow


/**
 * 카메라를 제어하는 인터페이스
 */
interface CameraController {
    suspend fun connectCamera(previewView: PreviewView): Result<Unit>

    var detectionCallback: CameraHelper.OnDetectionListener

    var fragmentLifeCycleOwner: LifecycleOwner
}
