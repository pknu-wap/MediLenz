package com.android.mediproject.core.model.ai

import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetail

data class ClassificationResult(
    val detectionObject: DetectionObject, val classificationRecognition: ClassificationRecognition,
) {
    var medicineDetail: MedicineDetail? = null
    var onClick: ((MedicineDetail) -> Unit)? = null
}
