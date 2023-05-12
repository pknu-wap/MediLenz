package com.android.mediproject.feature.camera.ai

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.view.Surface


class Yolo {
    external fun loadModel(mgr: AssetManager?, modelid: Int = 1, cpugpu: Int = 1): Boolean
    external fun openCamera(facing: Int): Boolean
    external fun closeCamera(): Boolean
    external fun setOutputWindow(surface: Surface?): Boolean
    external fun detectedObjects(): Array<DetectedObject>?

    companion object {
        init {
            System.loadLibrary("yolov8ncnn")
        }
    }
}

data class DetectedObject(val base64: String, val width: Int, val height: Int) {
    var onClicked: ((DetectedObject) -> Unit)? = null
    var bitmap: Bitmap? = null
}