package com.android.mediproject.core.model.medicine.InterestedMedicine


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddInterestedMedicineResponse(
    @SerialName("favoriteMedicineID") val favoriteMedicineID: Int, // 86
    @SerialName("message") val message: String
)