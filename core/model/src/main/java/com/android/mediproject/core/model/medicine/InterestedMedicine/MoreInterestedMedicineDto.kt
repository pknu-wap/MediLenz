package com.android.mediproject.core.model.medicine.InterestedMedicine

data class MoreInterestedMedicineDto(
    val id: Int,
    val itemName: String,
    val entpName: String,
    val itemIngrName: String,
    val spcltyPblc: String,
    val onClick : (() -> Unit)? = null
)
