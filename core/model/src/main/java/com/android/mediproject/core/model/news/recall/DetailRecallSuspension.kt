package com.android.mediproject.core.model.news.recall

import com.android.mediproject.core.annotation.UiModelMapping
import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.common.UiModel
import com.android.mediproject.core.model.common.UiModelMapper
import com.android.mediproject.core.model.toLocalDate


/**
 * 상세 회수/판매중지 데이터
 *
 * @property bizrNo 사업자등록번호
 * @property company 업체명
 * @property companyAddress 공장주소
 * @property companyTelNo 업체담당자전화번호
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
data class DetailRecallSuspension(
    val bizrNo: String, // 1278641862
    val company: String, // (주)자연세상
    val companyAddress: String,  // 경기도 포천시 소흘읍 호국로481번길 21-16
    val companyTelNo: String, // 0315443091
    val manufacturedDate: DateTimeValue, // 20211001
    val manufactureNo: String, // 330-061-21
    val openEndDate: DateTimeValue, // 20250922
    val packageUnit: String, // 자사포장단위
    val product: String, // 자연세상갈근
    val recallCommandDate: DateTimeValue, // 20220923
    val retrievalCommandDate: DateTimeValue, // 20221004000000
    val retrievalReason: String, // 정량법 부적합
    val usagePeriod: String, // 제조일로부터36개월
    val imageUrl: String,
) : UiModel

@UiModelMapping
class DetailRecallSaleSuspensionUiModelMapper(override val source: DetailRecallSaleSuspensionResponse.Item.Item) :
    UiModelMapper<DetailRecallSuspension>() {


    override fun convert(): DetailRecallSuspension {
        return source.run {
            DetailRecallSuspension(
                bizrNo = bizrNo,
                company = enterprise,
                companyAddress = entpAddress,
                companyTelNo = entpTelNo,
                manufacturedDate = manufacturedDate.toLocalDate("yyyyMMdd"),
                manufactureNo = manufactureNo,
                openEndDate = openEndDate.toLocalDate("yyyyMMdd"),
                packageUnit = packageUnit,
                product = product,
                recallCommandDate = recallCommandDate.toLocalDate("yyyyMMdd"),
                retrievalCommandDate = retrievalCommandDate.toLocalDate("yyyyMMddHHmmss"),
                retrievalReason = retrievalReason,
                usagePeriod = usagePeriod,
                imageUrl = imageUrl,
            )
        }
    }
}
