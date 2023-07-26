package com.android.mediproject.core.model.remote.elderlycaution


import com.android.mediproject.core.model.DataGoKrBaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElderlyCautionResponse(
    @SerialName("body") val body: Body,
) : DataGoKrBaseResponse() {
    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item>,
        @SerialName("numOfRows") val numOfRows: Int, // 15
        @SerialName("pageNo") val pageNo: Int, // 1
        @SerialName("totalCount") val totalCount: Int, // 1
    ) {
        /**
         * 노인안전정보
         *
         * @param changeDate 변경일자
         * @param cHART 모양
         * @param cLASSCODE 분류코드
         * @param className 분류명
         * @param entpName 업체명
         * @param etcOtcName 구분
         * @param formName 제형
         * @param ingrCode 성분코드
         * @param ingrEngName 성분영문명
         * @param iNGRENGNAMEFULL 성분영문명(전체)
         * @param ingrKorName 성분명
         * @param iTEMNAME 품목명
         * @param iTEMSEQ 품목일련번호
         * @param iTEMPERMITDATE 허가일자
         * @param mAININGR 성분내용
         * @param mixIngr 성분내용
         * @param mixType 성분유형
         * @param notificationDate 통지일자
         * @param prohibitContent 금기내용
         * @param remark 비고
         * @param typeName 유형
         */
        @Serializable
        data class Item(
            @SerialName("CHANGE_DATE") val changeDate: String, // 20221201
            @SerialName("CHART") val cHART: String, // 청색의 원형 필름코팅정
            @SerialName("CLASS_CODE") val cLASSCODE: String, // 01170
            @SerialName("CLASS_NAME") val className: String, // 정신신경용제
            @SerialName("ENTP_NAME") val entpName: String, // 환인제약(주)
            @SerialName("ETC_OTC_NAME") val etcOtcName: String, // 전문의약품
            @SerialName("FORM_NAME") val formName: String, // 필름코팅정
            @SerialName("INGR_CODE") val ingrCode: String, // D000809
            @SerialName("INGR_ENG_NAME") val ingrEngName: String, // Amitriptyline
            @SerialName("INGR_ENG_NAME_FULL") val iNGRENGNAMEFULL: String, // Amitriptyline(아미트리프틸린)
            @SerialName("INGR_NAME") val ingrKorName: String, // 아미트리프틸린
            @SerialName("ITEM_NAME") val iTEMNAME: String, // 에나폰정10밀리그램(아미트리프틸린염산염)
            @SerialName("ITEM_PERMIT_DATE") val iTEMPERMITDATE: String, // 19700220
            @SerialName("ITEM_SEQ") val iTEMSEQ: String, // 197000079
            @SerialName("MAIN_INGR") val mAININGR: String, // [M223101]아미트리프틸린염산염
            @SerialName("MIX_INGR") val mixIngr: String, // [M223101]아미트리프틸린염산염
            @SerialName("MIX_TYPE") val mixType: String, // 단일
            @SerialName("NOTIFICATION_DATE") val notificationDate: String, // 20150728
            @SerialName("PROHBT_CONTENT")
            val prohibitContent: String, // 노인에서의 삼환계 항우울제 사용은 기립성 저혈압, 비틀거림, 항콜린작용에 의한 구갈, 배뇨곤란, 변비, 안내압항진 등이 나타나기 쉬움으로 소량으로 신중투여
            @SerialName("REMARK") val remark: String?, // null
            @SerialName("TYPE_NAME") val typeName: String, // 노인주의
        )
    }

}
