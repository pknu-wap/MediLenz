package com.android.mediproject.core.model.ai

import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetail

data class ClassificationResultEntity(
    val detectionObject: DetectionResultEntity.Object, val classificationRecognitionEntity: ClassificationRecognitionEntity,
) {
    var medicineDetail: MedicineDetail? = null
    var onClick: ((MedicineDetail) -> Unit)? = null
}
