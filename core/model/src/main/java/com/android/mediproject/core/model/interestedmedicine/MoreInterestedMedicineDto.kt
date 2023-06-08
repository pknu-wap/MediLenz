package com.android.mediproject.core.model.interestedmedicine

data class MoreInterestedMedicineDto(
    val id: Int,
    val itemName: String,
    val entpName: String,
    val itemIngrName: String,
    val spcltyPblc: String
)

fun InterestedMedicineListResponse.Medicine.toMoreInterestedMedicineDto() =
    MoreInterestedMedicineDto(
        id = id,
        itemName = itemName,
        entpName = entpName,
        itemIngrName = itemIngrName,
        spcltyPblc = spcltyPblc
    )