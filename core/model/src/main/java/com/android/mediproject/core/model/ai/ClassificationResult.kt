package com.android.mediproject.core.model.ai

import android.graphics.Bitmap
import com.android.mediproject.core.model.common.UiModel
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetail

data class ClassificationResult(
    val items: List<Item>,
) : UiModel {
    data class Item(
        val itemSeq: String, val score: Int, val bitmap: Bitmap,
    ) {
        var medicineDetail: MedicineDetail? = null
    }
}
