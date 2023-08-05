package com.android.mediproject.core.model.ai

import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto

data class ClassificationResult(
    val detectionObject: DetectionObject, val classificationRecognition: ClassificationRecognition
) {
    var medicineDetatilInfoDto: MedicineDetatilInfoDto? = null
    var onClick: ((MedicineDetatilInfoDto) -> Unit)? = null
}