package com.android.mediproject.core.model.remote.recall


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 상세 리콜 폐기 데이터 응답 클래스
 *
 * @property body 상세 리콜 폐기 데이터
 * @property header 응답 헤더
 *
 */
@Serializable
data class DetailRecallSuspensionResponse(
    @SerialName("body") val body: Body,
) : DataGoKrBaseResponse() {

    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item>,
        @SerialName("numOfRows") val numOfRows: Int, // 15
        @SerialName("pageNo") val pageNo: Int, // 1
        @SerialName("totalCount") val totalCount: Int // 1
    ) {
        @Serializable
        data class Item(
            @SerialName("item") val item: Item
        ) {

            /**
             * 상세 리콜 폐기 데이터
             *
             * @property bIZRNO 사업자등록번호
             * @property eNTRPS 업체명
             * @property eNTRPSADRES 업체 주소
             * @property eNTRPSTELNO 업체 전화번호
             * @property manufacturedDate 제조일자
             * @property manufactureId 제조번호
             * @property openEndDate 폐기일자
             * @property pACKNGUNIT 포장단위
             * @property pRDUCT 제품명
             * @property recalLCommandDate 리콜명령일자
             * @property rM 비고
             * @property approvalDate 폐기명령일자
             * @property retrievalReason  폐기사유
             * @property usagePeriod 기간
             *
             */
            @Serializable
            data class Item(
                @SerialName("BIZRNO") val bIZRNO: String?, // 1278641862
                @SerialName("ENTRPS") val eNTRPS: String?, // (주)자연세상
                @SerialName("ENTRPS_ADRES") val eNTRPSADRES: String?, // 경기도 포천시 소흘읍 호국로481번길 21-16
                @SerialName("ENTRPS_TELNO") val eNTRPSTELNO: String?, // 0315443091
                @SerialName("MNFCTUR_DT") val manufacturedDate: String?, // 20211001
                @SerialName("MNFCTUR_NO") val manufactureId: String?, // 330-061-21
                @SerialName("OPEN_END_DATE") val openEndDate: String, // 20250922
                @SerialName("PACKNG_UNIT") val pACKNGUNIT: String?, // 자사포장단위
                @SerialName("PRDUCT") val pRDUCT: String?, // 자연세상갈근
                @SerialName("RECALL_COMMAND_DATE") val recalLCommandDate: String, // 20220923
                @SerialName("RM") val rM: String?, // null
                @SerialName("RTRVL_CMMND_DT") val approvalDate: String?, // 20221004000000
                @SerialName("RTRVL_RESN") val retrievalReason: String?, // 정량법 부적합
                @SerialName("USGPD") val usagePeriod: String? // 제조일로부터36개월
            )
        }


    }
}