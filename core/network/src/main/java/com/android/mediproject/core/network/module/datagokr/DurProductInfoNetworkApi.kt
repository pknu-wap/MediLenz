package com.android.mediproject.core.network.module.datagokr

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_DEFAULT_PAGE_NO
import com.android.mediproject.core.common.DATA_GO_KR_DEFAULT_ROW_SIZE
import com.android.mediproject.core.common.JSON
import com.android.mediproject.core.model.datagokr.durproduct.capacity.DurProductCapacityAttentionResponse
import com.android.mediproject.core.model.datagokr.durproduct.combination.DurProductCombinationTabooResponse
import com.android.mediproject.core.model.datagokr.durproduct.dosing.DurProductDosingCautionResponse
import com.android.mediproject.core.model.datagokr.durproduct.efficacygroupduplication.DurProductEfficacyGroupDuplicationResponse
import com.android.mediproject.core.model.datagokr.durproduct.extendedreleasetablet.DurProductExReleaseTableSplitAttentionResponse
import com.android.mediproject.core.model.datagokr.durproduct.pregnancy.DurProductPregnantWomanTabooResponse
import com.android.mediproject.core.model.datagokr.durproduct.productlist.DurProductListResponse
import com.android.mediproject.core.model.datagokr.durproduct.senior.DurProductSeniorCautionResponse
import com.android.mediproject.core.model.datagokr.durproduct.specialtyagegroup.DurProductSpecialtyAgeGroupTabooResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 의약품 안전 사용 서비스(DUR) 품목정보
 *
 * 식품의약품안전처에서 관리하는 의약품 안전사용정보 관련 조회
 * - 병용금기, 특정연령대금기, 임부금기, 용량주의, 투여기간주의, 노인주의, 효능군중복주의, 서방정분할주의, DUR품목정보 등
 */
interface DurProductInfoNetworkApi {

    /**
     * DUR 품목 정보 조회
     *
     * @param itemName 의약품명
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getDurPrdlstInfoList2")
    suspend fun getDurProductList(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductListResponse>

    /**
     * 노인 주의 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getOdsnAtentInfoList2")
    suspend fun getSeniorCaution(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("typeName") typeName: String = "노인주의",
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductSeniorCautionResponse>

    /**
     * 서방정 분할 주의 정보 조회
     *
     * @param itemName 의약품명
     * @param entpName 업체명
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     */
    @GET(value = "DURPrdlstInfoService02/getSeobangjeongPartitnAtentInfoList2")
    suspend fun getExReleaseTableSplitAttentionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("entpName") entpName: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductExReleaseTableSplitAttentionResponse>

    /**
     * 효능군 중복 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     */
    @GET(value = "DURPrdlstInfoService02/getEfcyDplctInfoList2")
    suspend fun getEfficacyGroupDuplicationInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductEfficacyGroupDuplicationResponse>

    /**
     * 투여기간 주의 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getMdctnPdAtentInfoList2")
    suspend fun getDosingCautionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductDosingCautionResponse>

    /**
     * 용량주의 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getCpctyAtentInfoList2")
    suspend fun getCapacityAttentionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductCapacityAttentionResponse>

    /**
     * 임산부 금기 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getPwnmTabooInfoList2")
    suspend fun getPregnantWomanTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductPregnantWomanTabooResponse>

    /**
     * 특정연령대금기 정보조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getSpcifyAgrdeTabooInfoList2")
    suspend fun getSpecialtyAgeGroupTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductSpecialtyAgeGroupTabooResponse>

    /**
     * 병용금기 정보 조회
     *
     * @param itemName 의약품명
     * @param ingrCode DUR성분코드
     * @param typeName DUR유형
     * @param itemSeq 품목기준코드
     *
     */
    @GET(value = "DURPrdlstInfoService02/getUsjntTabooInfoList02")
    suspend fun getCombinationTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_DEFAULT_PAGE_NO,
        @Query("itemName") itemName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("typeName") typeName: String?,
        @Query("itemSeq") itemSeq: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_DEFAULT_ROW_SIZE,
    ): Response<DurProductCombinationTabooResponse>
}
