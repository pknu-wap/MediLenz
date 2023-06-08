package com.android.mediproject.core.model.interestedmedicine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsInterestedMedicineResponse(
    @SerialName("isFavorite") val isFavorite: Boolean, // false
    @SerialName("message") val message: String
)