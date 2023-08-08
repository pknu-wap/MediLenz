package com.android.mediproject.core.model.news.recall


import com.android.mediproject.core.model.DataGoKrResponse
import com.android.mediproject.core.model.servercommon.NetworkApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 식약처 리콜 폐기 목록 응답을 받을 데이터 클래스
 *
 * @property body
 * @property header
 *
 */
@Serializable
class RecallSaleSuspensionListResponse : DataGoKrResponse<RecallSaleSuspensionListResponse.Item>() {

    @Serializable
    data class Item(
        @SerialName("item") val item: Item,
    ) {

        /**
         * 리콜 폐기 목록 별 데이터 클래스
         *
         * @property bizrNo 사업자등록번호
         * @property enforced 강제여부
         * @property enterprise 업체명
         * @property itemSeq 일련번호
         * @property product 품목명
         * @property recallCommandDate 회수명령일자
         * @property retrievalCommandDate 승인일자
         * @property retrievalReason  회수사유내용
         *
         */
        @Serializable
        data class Item(
            @SerialName("BIZRNO") val bizrNo: String = "", // 3018508241
            @SerialName("ENFRC_YN") val enforced: String = "", // N
            @SerialName("ENTRPS") val enterprise: String = "", // 동국제약(주)
            @SerialName("ITEM_SEQ") val itemSeq: String = "", // 201208461
            @SerialName("PRDUCT") val product: String = "", // 니자틴캡슐(니자티딘)
            @SerialName("RECALL_COMMAND_DATE") val recallCommandDate: String = "", // 20230504
            @SerialName("RTRVL_CMMND_DT") val retrievalCommandDate: String = "", // 20230503000000
            @SerialName("RTRVL_RESN") val retrievalReason: String = "", // 안정성 시험결과 NDMA 기준 초과에 따른 사전예방적 조치로 시중 유통품에 대해 영업자회수
        ) : NetworkApiResponse.ListItem {
            var imageUrl: String = ""
        }
    }

}
