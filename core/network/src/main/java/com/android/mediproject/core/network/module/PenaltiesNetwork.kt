package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PenaltiesNetwork {

    @Provides
    @Singleton
    fun providesPenaltiesNetworkApi(okHttpCallFactory: Call.Factory): PenaltiesNetworkApi =
        Retrofit.Builder().callFactory(okHttpCallFactory).addConverterFactory(
            Json.asConverterFactory("application/json".toMediaType())
        ).baseUrl(DATA_GO_KR_BASEURL).build().create(PenaltiesNetworkApi::class.java)


    @Provides
    @Singleton
    fun providesRecallSuspensionDataSource(
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher, penaltiesNetworkApi: PenaltiesNetworkApi
    ): RecallSuspensionDataSource = RecallSuspensionDataSourceImpl(ioDispatcher, penaltiesNetworkApi)

}


interface PenaltiesNetworkApi {

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgelList02")
    suspend fun getRecallSuspensionList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): RecallSuspensionListResponse

    /**
     * 의약품 회수·판매중지 정보 상세 조회
     *
     * @param company 제조사
     * @param product 제품명
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgeItem02")
    suspend fun getDetailRecallSuspensionInfo(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("Entrps") company: String?,
        @Query("Prduct") product: String?,
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): DetailRecallSuspensionResponse

    /**
     * 행정 처분 목록조회
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgelList02")
    suspend fun getAdminActionList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("order") order: String = "Y",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): AdminActionListResponse
}