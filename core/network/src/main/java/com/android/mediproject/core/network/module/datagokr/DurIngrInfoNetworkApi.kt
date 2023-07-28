package com.android.mediproject.core.network.module.datagokr

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_NO
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.JSON
import com.android.mediproject.core.model.dur.duringr.capacity.DurIngrCapacityAttentionResponse
import com.android.mediproject.core.model.dur.duringr.combination.DurIngrCombinationTabooResponse
import com.android.mediproject.core.model.dur.duringr.dosing.DurIngrDosingCautionResponse
import com.android.mediproject.core.model.dur.duringr.pregnancy.DurIngrPregnantWomanTabooResponse
import com.android.mediproject.core.model.dur.duringr.senior.DurIngrSeniorCautionResponse
import com.android.mediproject.core.model.dur.duringr.specialtyagegroup.DurIngrSpecialtyAgeGroupTabooResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 식품의약품안전처_의약품안전사용서비스(DUR)성분정보
 *
 * - 식품의약품안전처에서 관리하는 의약품(허가,행정,성분 등)의 관련 정보 조회
 */
interface DurIngrInfoNetworkApi {

    /**
     * DUR 병용금기 목록 조회
     *
     * @param ingrKorName DUR성분명(ex : 이트라코나졸)
     * @param ingrCode DUR성분(ex : D000762)
     */
    @GET(value = "DURIrdntInfoService02/getUsjntTabooInfoList01")
    suspend fun getCombinationTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "병용금기",
        @Query("ingrKorName") ingrKorName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrCombinationTabooResponse>


    /**
     * 특정연령대금기 목록 조회
     *
     * @param ingrName DUR성분명(ex : 아카보즈)
     * @param ingrCode DUR성분(ex : D000149)
     */
    @GET(value = "DURIrdntInfoService02/getSpcifyAgrdeTabooInfoList01")
    suspend fun getSpecialtyAgeGroupTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "특정연령대금기",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrSpecialtyAgeGroupTabooResponse>

    /**
     * 임부금기 목록 조회
     *
     * @param ingrName DUR성분명(ex : 아카보즈)
     * @param ingrCode DUR성분(ex : D000149)
     */
    @GET(value = "DURIrdntInfoService02/getPwnmTabooInfoList01")
    suspend fun getPregnantWomanTabooInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "임부금기",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrPregnantWomanTabooResponse>

    /**
     * 용량주의 정보 조회
     *
     * @param ingrName DUR성분명(ex : 트리아졸람)
     * @param ingrCode DUR성분(ex : D000592)
     */
    @GET(value = "DURIrdntInfoService02/getCpctyAtentInfoList01")
    suspend fun getCapacityAttentionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "용량주의",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrCapacityAttentionResponse>

    /**
     * 투여기간 정보 조회
     *
     * @param ingrName DUR성분명(ex : 트리아졸람)
     * @param ingrCode DUR성분(ex : D000592)
     */
    @GET(value = "DURIrdntInfoService02/getMdctnPdAtentInfoList01")
    suspend fun getDosingCautionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "투여기간주의",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrDosingCautionResponse>


    /**
     * 노인주의 정보 조회
     *
     * @param ingrName DUR성분명(ex : 클로르디아제폭시드)
     * @param ingrCode DUR성분(ex : D000056)
     */
    @GET(value = "DURIrdntInfoService02/getOdsnAtentInfoList01")
    suspend fun getSeniorCaution(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "노인주의",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrSeniorCautionResponse>

    /**
     * 효능군중복주의 정보 조회
     *
     * @param ingrName DUR성분명(ex : 아세클로페낙)
     * @param ingrCode DUR성분(ex : D000739)
     */
    @GET(value = "DURIrdntInfoService02/getEfcyDplctInfoList01")
    suspend fun getEfficacyGroupDuplicationInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int = DATA_GO_KR_PAGE_NO,
        @Query("typeName") typeName: String = "효능군중복",
        @Query("ingrName") ingrName: String?,
        @Query("ingrCode") ingrCode: String?,
        @Query("type") type: String = JSON,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE,
    ): Response<DurIngrCombinationTabooResponse>
}
