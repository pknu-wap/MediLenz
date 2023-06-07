package com.android.mediproject.core.model.medicine.interestedMedicine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InterestedMedicineListResponse(
    val medicineList: List<Medicine>,
    val message: String
) {
    @Serializable
    data class Medicine(
        @SerialName("ENTP_NAME")
        val entpName: String,
        @SerialName("ID")
        val id: Int,
        @SerialName("ITEM_INGR_NAME")
        val itemIngrName: String,
        @SerialName("ITEM_NAME")
        val itemName: String,
        @SerialName("ITEM_SEQ")
        val itemSeq: String,
        @SerialName("PRDUCT_TYPE")
        val prductType: String,
        @SerialName("SPCLTY_PBLC")
        val spcltyPblc: String
    )
}

fun InterestedMedicineListResponse.Medicine.toInterestedMedicineDto() =
    InterestedMedicineDto(itemSeq = itemSeq, medicineName = itemName)