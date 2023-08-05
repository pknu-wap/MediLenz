package com.android.mediproject.core.model.ai


import kotlinx.serialization.Serializable

@Serializable
data class VertexAiPredictResponse(
    val deployedModelId: String, // 5635148824956633088
    val model: String, // projects/82486562104/locations/us-central1/models/3645358234123894784
    val modelDisplayName: String, // medicines_classification2
    val modelVersionId: String, // 1
    val predictions: List<Prediction>) : BaseVertexAiResponse() {
    @Serializable
    data class Prediction(
        val confidences: List<Double>, val displayNames: List<String>, val ids: List<String>)
}