package com.android.mediproject.core.model.news.safetynotification

import com.android.mediproject.core.annotation.UiModelMapping
import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.common.UiModel
import com.android.mediproject.core.model.common.UiModelMapper
import com.android.mediproject.core.model.toLocalDate


/**
 * 안전성정보
 *
 * @property actions 조치사항
 * @property attachmentFile 첨부파일
 * @property department 담당부서
 * @property views 조회수
 * @property informationDetails 내용
 * @property notificationClassId 구분코드
 * @property notificationClassName 구분명
 * @property publicationDate 공개시작일
 * @property manager 담당자
 * @property publicationStartDateTime 공개시작일시
 * @property safetyNotificationNo 안전성 서한번호
 * @property informationSummary 안전성 서한 정보요약
 * @property title 제목
 */
data class SafetyNotification(
    val actions: String = "", // ❍ 한국휴텍스제약㈜에서 제조한 ‘레큐틴정’ 등 6개 품목(붙임)
    val attachmentFile: String = "", // https://nedrug.mfds.go.kr/cmn/edms/down/1ObfzbuFq4l
    val department: String = "", // 의약품관리과
    val views: String = "", // 402
    val informationDetails: String = "", // ❍ 식품의약품안전처는 한국휴텍스제약㈜에 대한 현장조사 결과, ‘레큐틴정’ 등 6개 품목이 허가 또는 신고된 사항과 다르게 제조되고 있는 사실이 확인됨에 따라 사전 예방적 차원에서
    // 잠정 제조·판매 중지를 명령하고 해당 품목에 대하여 회수 조치함.❍ 의․약전문가는 동 정보사항에 유의하여 해당 제품의 처방 및 사용을 중지하고 필요시 대체의약품을 사용하여 주실 것을 당부드리며,❍ 아울러 해당 유통품 회수가 적절히 수행될 수 있도록 적극 협조하여 주시기 바람.
    val notificationClassId: String = "", // 01
    val notificationClassName: String = "", // 안전성서한
    val publicationDate: DateTimeValue, // 2023-07-21
    val manager: String = "", // 정다현
    val publicationStartDateTime: DateTimeValue, // 2023-07-21 00:00:00
    val safetyNotificationNo: String = "", // 500
    val informationSummary: String = "", // ❍ 한국휴텍스제약㈜에서 제조한 의약품 6개 품목에 대하여 잠정 제조·판매중지 명령 및 사용 중단을 요청함.
    val title: String = "", // 한국휴텍스제약㈜ 제조 ‘레큐틴정(트리메부틴말레산염)’등 6개 품목 잠정 제조·판매·사용 중지
    var onClick: ((SafetyNotification) -> Unit)? = null,
) : UiModel

@UiModelMapping
class SafetyNotificationUiModelMapper(override val source: SafetyNotificationResponse.Item) : UiModelMapper<SafetyNotification>() {


    override fun convert(): SafetyNotification = source.run {
        SafetyNotification(
            actions = actions,
            attachmentFile = attachmentFile,
            department = department,
            views = views,
            informationDetails = informationDetails,
            notificationClassId = notificationClassId,
            notificationClassName = notificationClassName,
            publicationDate = publicationDate.toLocalDate("yyyy-MM-dd"),
            manager = manager,
            publicationStartDateTime = publicationStartDateTime.toLocalDate("yyyy-MM-dd HH:mm:ss"),
            safetyNotificationNo = safetyNotificationNo,
            informationSummary = informationSummary,
            title = title,
        )
    }
}
