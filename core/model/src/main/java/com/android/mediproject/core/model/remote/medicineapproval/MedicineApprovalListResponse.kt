package com.android.mediproject.core.model.remote.medicineapproval

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 식약처 의약품 허가 목록 응답을 받을 데이터 클래스
 *
 * @property body
 * @property header
 *
 */
@Serializable
data class MedicineApprovalListResponse(
    @SerialName("body") val body: Body?, @SerialName("header") val header: Header
)

@Serializable
data class Body(
    @SerialName("items") val items: List<Item>, @SerialName("numOfRows") val numOfRows: Int, // 15
    @SerialName("pageNo") val pageNo: Int, // 1
    @SerialName("totalCount") val totalCount: Int // 245
)

@Serializable
data class Header(
    @SerialName("resultCode") val resultCode: String, // 00
    @SerialName("resultMsg") val resultMsg: String // NORMAL SERVICE.
)

/**
 * 의약품 별 데이터 클래스
 * @property itemSeq 일련번호
 * @property itemName 한글 의약품 명
 * @property itemEngName 영문 의약품 명
 * @property entpName 업체명
 * @property entpEngName 업체 영문명
 * @property entpSeq 업체 일련번호
 * @property entpNo 업체 번호
 * @property itemPermitDate 아이템 허가 일자
 * @property induty 업종
 * @property prdlstStdrCode 제품표준코드
 * @property spcltyPblc 특수약품 구분
 * @property prductType 제품유형
 * @property prductPrmisnNo 제품허가번호
 * @property itemIngrName 성분명
 * @property itemIngrCnt 성분수
 * @property bigPrdtImgUrl 대표 제품 이미지 URL
 * @property permitKindCode 허가종류코드
 * @property cancelDate 취소 일자
 * @property cancelName 취소 여부
 * @property ediCode EDI코드
 * @property bizrno 사업자등록번호
 *
 */
@Serializable
data class Item(
    @SerialName("BIG_PRDT_IMG_URL") val bigPrdtImgUrl: String?,
    @SerialName("BIZRNO") val bizrno: String?,
    @SerialName("CANCEL_DATE") val cancelDate: String?,
    @SerialName("CANCEL_NAME") val cancelName: String?,
    @SerialName("EDI_CODE") val ediCode: String?,
    @SerialName("ENTP_ENG_NAME") val entpEngName: String?,
    @SerialName("ENTP_NAME") val entpName: String?,
    @SerialName("ENTP_NO") val entpNo: String?,
    @SerialName("ENTP_SEQ") val entpSeq: String?,
    @SerialName("INDUTY") val induty: String?,
    @SerialName("ITEM_ENG_NAME") val itemEngName: String?,
    @SerialName("ITEM_INGR_CNT") val itemIngrCnt: String?,
    @SerialName("ITEM_INGR_NAME") val itemIngrName: String?,
    @SerialName("ITEM_NAME") val itemName: String?,
    @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String?,
    @SerialName("ITEM_SEQ") val itemSeq: String?,
    @SerialName("PERMIT_KIND_CODE") val permitKindCode: String?,
    @SerialName("PRDLST_STDR_CODE") val prdlstStdrCode: String?,
    @SerialName("PRDUCT_PRMISN_NO") val prductPrmisnNo: String?,
    @SerialName("PRDUCT_TYPE") val prductType: String?,
    @SerialName("SPCLTY_PBLC") val spcltyPblc: String?
)