package com.android.mediproject.core.model.local.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 약 목록에서 약 정보 화면으로 이동 시 전달할 데이터
 *
 * @param entpKorName 약 제조사 : (주)한국얀센
 * @param entpEngName 약 제조사 영문 : Janssen Korea.
 * @param itemIngrName 약 성분 : 덱스트로메토르판브롬화수소산염수화물/슈도에페드린염산염/아세트아미노펜/클로르페니라민말레인산염
 * @param itemKorName 약 이름 : 타이레놀콜드-에스정(수출명:TylenolColdTablet,TylenolColdCaplet)
 * @param itemEngName 약 영문 이름 : TylenolColdTablet
 * @param itemSeq 약 품목 기준 코드 : 200302348
 * @param productType 약 품목 : [01140]해열.진통.소염제
 * @param medicineType 약 분류 : [01]일반의약품
 * @param imgUrl 약 이미지 URL : http://drug.mfds.go.kr/html/images/200302348.jpg
 */
@Parcelize
data class MedicineInfoArgs(
    val entpKorName: String,
    val entpEngName: String,
    val itemIngrName: String,
    val itemKorName: String,
    val itemEngName: String,
    val itemSeq: Long,
    val productType: String,
    val imgUrl: String,
    val medicineType: String) : BaseNavArgs(MedicineInfoArgs::class.java.name), Parcelable