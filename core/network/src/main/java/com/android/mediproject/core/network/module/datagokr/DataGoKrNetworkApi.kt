package com.android.mediproject.core.network.module.datagokr

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_NO
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.JSON
import com.android.mediproject.core.model.granule.GranuleIdentificationInfoResponse
import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.news.adminaction.AdminActionListResponse
import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.model.news.recall.RecallSaleSuspensionListResponse
import com.android.mediproject.core.model.news.safetynotification.SafetyNotificationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface DataGoKrNetworkApi : DurProductInfoNetworkApi, DurIngrInfoNetworkApi {
    /**
     * 의약품 허가 목록 조회
     *
     * @param itemName 의약품명
     * @param entpName 업체명
     * @param medicationType 의약품 분류(전문의약품, 일반의약품)
     */
    @GET(value = "DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnInq04")
    suspend fun getApprovalList(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("item_name", encoded = false) itemName: String?,
        @Query("entp_name", encoded = false) entpName: String?,
        @Query("spclty_pblc", encoded = true) medicationType: String?,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<MedicineApprovalListResponse>


    /**
     * 의약품 허가 정보 상세 조회
     *
     * @param itemName 의약품명
     * @param itemSeq 품목기준코드
     */
    @GET(value = "DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnDtlInq03")
    suspend fun getMedicineDetailInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("item_name", encoded = false) itemName: String = "",
        @Query("item_seq", encoded = true) itemSeq: String = "",
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = 1,
    ): Response<String>

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgelList02")
    suspend fun getRecallSuspensionList(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<RecallSaleSuspensionListResponse>

    /**
     * 의약품 회수·판매중지 정보 상세 조회
     *
     * @param company 제조사
     * @param product 제품명
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgeItem02")
    suspend fun getDetailRecallSuspensionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("type") type: String = JSON,
        @Query("Entrps", encoded = true) company: String?,
        @Query("Prduct", encoded = true) product: String?,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DetailRecallSaleSuspensionResponse>

    /**
     * 행정 처분 목록조회
     */
    @GET(value = "MdcinExaathrService04/getMdcinExaathrList04")
    suspend fun getAdminActionList(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = JSON,
        @Query("order", encoded = true) order: String = "Y",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<AdminActionListResponse>

    /**
     * 의약품 낱알 식별 정보 조회
     *
     * @param itemName 의약품명
     * @param entpName 업체명
     * @param itemSeq 품목기준코드
     */
    @GET(value = "MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
    suspend fun getGranuleIdentificationInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("item_name") itemName: String?,
        @Query("entp_name") entpName: String?,
        @Query("item_seq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<GranuleIdentificationInfoResponse>

    @GET(value = "DrugSafeLetterService02/getDrugSafeLetterList02")
    suspend fun getDrugSafeLetterList(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<SafetyNotificationResponse>
}
