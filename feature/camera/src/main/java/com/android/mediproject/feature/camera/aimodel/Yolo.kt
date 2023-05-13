package com.android.mediproject.feature.camera.aimodel

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.view.Surface


class Yolo {
    external fun loadModel(mgr: AssetManager?, modelid: Int = 1, cpugpu: Int = 0): Boolean
    external fun openCamera(facing: Int): Boolean
    external fun closeCamera(): Boolean
    external fun setOutputWindow(surface: Surface?): Boolean
    external fun detectedObjects(): Array<DetectedObject>?
    external fun getCurrentImage(): DetectedImage

    companion object {
        init {
            System.loadLibrary("yolov8ncnn")
        }
    }
}


data class DetectedObject(val base64: String, val width: Int, val height: Int) {
    var onClicked: (() -> Unit)? = null
    var bitmap: Bitmap? = null
}

data class DetectedImage(val base64: String, val width: Int, val height: Int)