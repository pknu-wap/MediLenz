package com.android.mediproject.core.model.recall


import com.android.mediproject.core.model.DataGoKrResponse
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
class DetailRecallSaleSuspensionResponse : DataGoKrResponse<DetailRecallSaleSuspensionResponse.Item>() {

    @Serializable
    data class Item(
        @SerialName("item") val item: Item,
    ) {

        /**
         * 상세 회수/판매중지 데이터
         *
         * @property bizrNo 사업자등록번호
         * @property enterprise 업체명
         * @property entpAddress 공장주소
         * @property entpTelNo 업체담당자전화번호
         * @property manufacturedDate 제조일자
         * @property manufactureNo 제조번호
         * @property openEndDate 공개마감일
         * @property packageUnit 포장단위
         * @property product 품목명
         * @property recallCommandDate 회수명령일자
         * @property retrievalCommandDate 승인일자
         * @property retrievalReason  회수사유내용
         * @property usagePeriod 유효기간
         *
         */
        @Serializable
        data class Item(
            @SerialName("BIZRNO") val bizrNo: String = "", // 1278641862
            @SerialName("ENTRPS") val enterprise: String = "", // (주)자연세상
            @SerialName("ENTRPS_ADRES") val entpAddress: String = "", // 경기도 포천시 소흘읍 호국로481번길 21-16
            @SerialName("ENTRPS_TELNO") val entpTelNo: String = "", // 0315443091
            @SerialName("MNFCTUR_DT") val manufacturedDate: String = "", // 20211001
            @SerialName("MNFCTUR_NO") val manufactureNo: String = "", // 330-061-21
            @SerialName("OPEN_END_DATE") val openEndDate: String = "", // 20250922
            @SerialName("PACKNG_UNIT") val packageUnit: String = "", // 자사포장단위
            @SerialName("PRDUCT") val product: String = "", // 자연세상갈근
            @SerialName("RECALL_COMMAND_DATE") val recallCommandDate: String = "", // 20220923
            @SerialName("RTRVL_CMMND_DT") val retrievalCommandDate: String = "", // 20221004000000
            @SerialName("RTRVL_RESN") val retrievalReason: String = "", // 정량법 부적합
            @SerialName("USGPD") val usagePeriod: String = "", // 제조일로부터36개월
        )
    }

}
