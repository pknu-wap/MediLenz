package com.android.mediproject.core.model.medicine.interestedmedicine

data class InterestedMedicineDto (
    val itemSeq : String,
    val medicineName: String
)

fun InterestedMedicineListResponse.Medicine.toInterestedMedicineDto() =
    InterestedMedicineDto(itemSeq = itemSeq, medicineName = itemName)