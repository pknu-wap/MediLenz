package com.android.mediproject.core.model.medicine.safetynotification


import com.android.mediproject.core.model.DataGoKrResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SafetyNotificationResponse : DataGoKrResponse<SafetyNotificationResponse.Item>() {

    @Serializable
    data class Item(
        @SerialName("ACTN_MTTR_CONT") val actions: String = "", // ❍ 한국휴텍스제약㈜에서 제조한 ‘레큐틴정’ 등 6개 품목(붙임)
        @SerialName("ATTACH_FILE_URL") val attachmentFile: String = "", // https://nedrug.mfds.go.kr/cmn/edms/down/1ObfzbuFq4l
        @SerialName("CHRG_DEP") val department: String = "", // 의약품관리과
        @SerialName("INQ_CNT") val views: String = "", // 402
        @SerialName("PBANC_CONT")
        val informationDetails: String = "", // ❍ 식품의약품안전처는 한국휴텍스제약㈜에 대한 현장조사 결과, ‘레큐틴정’ 등 6개 품목이 허가 또는 신고된 사항과 다르게 제조되고 있는 사실이 확인됨에 따라 사전 예방적 차원에서
        // 잠정 제조·판매 중지를 명령하고 해당 품목에 대하여 회수 조치함.❍ 의․약전문가는 동 정보사항에 유의하여 해당 제품의 처방 및 사용을 중지하고 필요시 대체의약품을 사용하여 주실 것을 당부드리며,❍ 아울러 해당 유통품 회수가 적절히 수행될 수 있도록 적극 협조하여 주시기 바람.
        @SerialName("PBANC_DIVS_CD") val notificationClassId: String = "", // 01
        @SerialName("PBANC_DIVS_NM") val notificationClassName: String = "", // 안전성서한
        @SerialName("PBANC_YMD") val publicationDate: String = "", // 2023-07-21
        @SerialName("RGSTN_ID") val manager: String = "", // 정다현
        @SerialName("RLS_BGNG_YMD") val publicationStartDateTime: String = "", // 2023-07-21 00:00:00
        @SerialName("SAFT_LETT_NO") val safetyNotificationNo: String = "", // 500
        @SerialName("SUMRY_CONT") val informationSummary: String = "", // ❍ 한국휴텍스제약㈜에서 제조한 의약품 6개 품목에 대하여 잠정 제조·판매중지 명령 및 사용 중단을 요청함.
        @SerialName("TITLE") val title: String = "", // 한국휴텍스제약㈜ 제조 ‘레큐틴정(트리메부틴말레산염)’등 6개 품목 잠정 제조·판매·사용 중지
    )
}
