package com.android.mediproject.core.model.interestedmedicine


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewInterestedMedicineResponse(
    @SerialName("favoriteMedicineID") val favoriteMedicineID: Int, // 86
    @SerialName("message") val message: String
)