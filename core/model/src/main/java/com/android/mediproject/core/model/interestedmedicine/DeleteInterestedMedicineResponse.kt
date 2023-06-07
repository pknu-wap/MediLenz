package com.android.mediproject.core.model.interestedmedicine


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteInterestedMedicineResponse(
    @SerialName("message") val message: String
)