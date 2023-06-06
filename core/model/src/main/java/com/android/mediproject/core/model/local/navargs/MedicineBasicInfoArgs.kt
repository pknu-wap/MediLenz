package com.android.mediproject.core.model.local.navargs

/**
 * 댓글 화면으로 전달할 인자
 *
 * @property itemSeq 약 품목기준코드
 */
data class MedicineBasicInfoArgs(
    val itemSeq: Long, val medicineIdInAws: Long) : BaseNavArgs(MedicineBasicInfoArgs::class.java.name)