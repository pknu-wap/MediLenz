package com.android.mediproject.core.model.remote.recall


import com.android.mediproject.core.model.DataGoKrBaseResponse
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
data class RecallSuspensionListResponse(
    @SerialName("body") val body: Body?,
) : DataGoKrBaseResponse() {

    @Serializable
    data class Body(
        @SerialName("items") val items: List<Item>,
        @SerialName("numOfRows") val numOfRows: Int, // 15
        @SerialName("pageNo") val pageNo: Int, // 1
        @SerialName("totalCount") val totalCount: Int // 1015
    ) {

        @Serializable
        data class Item(
            @SerialName("item") val item: Item
        ) {

            /**
             * 리콜 폐기 목록 별 데이터 클래스
             *
             * @property bizrno 사업자등록번호
             * @property enfrcYn 강제여부
             * @property entrps 업체명
             * @property itemSeq 일련번호
             * @property product 제품명
             * @property recallCommandDate 리콜 명령 일자
             * @property rtrlCommandDt 폐기 명령 일자
             * @property rtrvlResn 폐기 사유
             *
             */
            @Serializable
            data class Item(
                @SerialName("BIZRNO") val bizrno: String?, // 3018508241
                @SerialName("ENFRC_YN") val enfrcYn: String?, // N
                @SerialName("ENTRPS") val entrps: String?, // 동국제약(주)
                @SerialName("ITEM_SEQ") val itemSeq: String?, // 201208461
                @SerialName("PRDUCT") val product: String?, // 니자틴캡슐(니자티딘)
                @SerialName("RECALL_COMMAND_DATE") val recallCommandDate: String?, // 20230504
                @SerialName("RTRVL_CMMND_DT") val rtrlCommandDt: String?, // 20230503000000
                @SerialName("RTRVL_RESN") val rtrvlResn: String? // 안정성 시험결과 NDMA 기준 초과에 따른 사전예방적 조치로 시중 유통품에 대해 영업자회수
            )
        }
    }
}