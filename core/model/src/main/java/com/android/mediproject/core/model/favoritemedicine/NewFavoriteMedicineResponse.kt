package com.android.mediproject.core.model.favoritemedicine


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewFavoriteMedicineResponse(
    @SerialName("favoriteMedicineID") val favoriteMedicineID: Int, // 86
    @SerialName("message") val message: String
)