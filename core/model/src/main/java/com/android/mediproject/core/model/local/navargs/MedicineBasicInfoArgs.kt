package com.android.mediproject.core.model.local.navargs

/**
 * 댓글 화면으로 전달할 인자
 *
 * @property medicineName 약 이름
 * @property itemSequence 약 품목기준코드
 */
data class MedicineBasicInfoArgs(
    val medicineName: String, val itemSequence: String
) : BaseNavArgs(MedicineBasicInfoArgs::class.java.name)