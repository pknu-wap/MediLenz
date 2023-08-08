package com.android.mediproject.core.model.news.recall


import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.common.UiModel
import com.android.mediproject.core.model.common.UiModelMapper
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.model.toLocalDate


/**
 * 리콜 폐기 목록 별 데이터 클래스
 *
 * @property enforced 강제여부
 * @property company 업체명
 * @property itemSeq 일련번호
 * @property product 품목명
 * @property recallCommandDate 회수명령일자
 * @property retrievalCommandDate 승인일자
 * @property retrievalReason 회수사유내용
 *
 */
data class RecallSaleSuspension(
    val enforced: String, // N
    val company: String = "동국제약(주)", // 동국제약(주)
    val itemSeq: String, // 201208461
    val product: String = "니자틴캡슐(니자티딘)", // 니자틴캡슐(니자티딘)
    val recallCommandDate: DateTimeValue, // 20230504
    val retrievalCommandDate: DateTimeValue, // 20230503000000
    val retrievalReason: String, // 안정성 시험결과 NDMA 기준 초과에 따른 사전예방적 조치로 시중 유통품에 대해 영업자회수
    val imageUrl: String,
    var onClick: ((RecallSaleSuspension) -> Unit)? = null,
) : UiModel

class RecallSaleSuspensionListUiModelMapper(override val source: RecallSaleSuspensionListResponse.Item.Item) :
    UiModelMapper<RecallSaleSuspension>() {

    private companion object {
        init {
            UiModelMapperFactory.register(RecallSaleSuspensionListUiModelMapper::class, RecallSaleSuspensionListResponse.Item.Item::class)
        }
    }

    override fun convert(): RecallSaleSuspension {
        return source.run {
            RecallSaleSuspension(
                enforced = enforced,
                company = enterprise,
                itemSeq = itemSeq,
                product = product,
                recallCommandDate = recallCommandDate.toLocalDate("yyyyMMdd"),
                retrievalCommandDate = retrievalCommandDate.toLocalDate("yyyyMMddHHmmss"),
                retrievalReason = retrievalReason,
                imageUrl = "",
            )
        }

    }

}
