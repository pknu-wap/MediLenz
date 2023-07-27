package com.android.mediproject.core.model.dur.durproduct.productlist


import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.dur.DurType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DurProductListResponse : DataGoKrResponse<DurProductListResponse.Item>() {


    /**
     * @param barCode 표준코드
     * @param bizrNo 사업자등록번호
     * @param cancelDate 취소일자
     * @param cancelName 취소사유
     * @param changeDate 변경일자
     * @param chart 성상
     * @param classNo 분류번호
     * @param ediCode 보험코드
     * @param entpName 업체명
     * @param itemName 제품명
     * @param itemPermitDate 품목허가일자
     * @param itemSeq 품목기준코드
     * @param materialName 원료성분
     * @param packUnit 포장단위
     * @param storageMethod 저장방법
     * @param typeName DUR 유형
     */
    @Serializable
    data class Item(
        @SerialName("BAR_CODE") val barCode: String = "", // 8806421025729
        @SerialName("BIZRNO") val bizrNo: String = "", // 1188100601
        @SerialName("CANCEL_DATE") val cancelDate: String = "", // null
        @SerialName("CANCEL_NAME") val cancelName: String = "", // 정상
        @SerialName("CHANGE_DATE") val changeDate: String = "", // 2019August26th
        @SerialName("CHART") val chart: String = "", // 미황색의 원형 정제
        @SerialName("CLASS_NO") val classNo: String = "", // [141]항히스타민제
        @SerialName("EDI_CODE") val ediCode: String = "", // 642102570
        @SerialName("ENTP_NAME") val entpName: String = "", // (주)유한양행
        @SerialName("ITEM_NAME") val itemName: String = "", // 페니라민정(클로르페니라민말레산염)
        @SerialName("ITEM_PERMIT_DATE") val itemPermitDate: String = "", // 1960October10th
        @SerialName("ITEM_SEQ") val itemSeq: String = "", // 196000011
        @SerialName("MATERIAL_NAME") val materialName: String = "", // 클로르페니라민말레산염,,2.0,밀리그램,KP,
        @SerialName("PACK_UNIT") val packUnit: String = "", // 1000정/병
        @SerialName("STORAGE_METHOD") val storageMethod: String = "", // 실온, 건소, 밀폐용기,
        @SerialName("TYPE_NAME  ") val typeName: String = "", // 용량주의,노인주의,첨가제주의
    ) : LeafItem {
        val typeNames = typeName.split(",").filterNot { it == DurType.excludeType }
    }
}
