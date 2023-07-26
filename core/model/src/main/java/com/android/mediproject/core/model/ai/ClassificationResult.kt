package com.android.mediproject.core.model.ai

import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfo

data class ClassificationResult(
    val detectionObject: DetectionObject, val classificationRecognition: ClassificationRecognition,
) {
    var medicineDetailInfo: MedicineDetailInfo? = null
    var onClick: ((MedicineDetailInfo) -> Unit)? = null
}
