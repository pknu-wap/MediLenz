package com.android.mediproject.core.model.local.navargs

/**
 * 약 정보 화면으로 전달할 인자
 *
 * @property medicineName 약 이름
 * @property imgUrl 약 이미지 URL
 * @property entpName 약 제조사
 * @property itemSequence 약 품목기준코드
 */
data class MedicineInfoArgs(
    val medicineName: String, val imgUrl: String, val entpName: String, val itemSequence: String, val medicineEngName: String
) : BaseNavArgs(MedicineInfoArgs::class.java.name)