package com.android.mediproject.core.model.requestparameters


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VertexAiPredictParameter(
    @SerialName("instances") val instances: List<Instance>,
    @SerialName("parameters") val parameters: Parameters
) {
    @Serializable
    data class Instance(
        @SerialName("content") val content: String
    )

    @Serializable
    data class Parameters(
        @SerialName("confidenceThreshold") val confidenceThreshold: Double = 0.4,
        @SerialName("maxPredictions") val maxPredictions: Int = 1
    )
}