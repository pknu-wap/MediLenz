package com.android.mediproject.core.model.ai

import org.tensorflow.lite.task.gms.vision.detector.Detection

data class DetectionObjects(val detection: List<Detection>, val width: Int, val height: Int)