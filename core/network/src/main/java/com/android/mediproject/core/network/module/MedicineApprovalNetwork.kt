package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.remote.adminaction.AdminActionListResponse
import com.android.mediproject.core.model.remote.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.remote.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.model.remote.recall.DetailRecallSuspensionResponse
import com.android.mediproject.core.model.remote.recall.RecallSuspensionListResponse
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionDataSource
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

const val DATA_GO_KR_BASEURL = "https://apis.data.go.kr/1471000/"

@InstallIn(SingletonComponent::class)
@Module
object MedicineApprovalNetwork {

    @Provides
    @Singleton
    fun providesDataGoKrNetworkApi(okHttpCallFactory: Call.Factory): DataGoKrNetworkApi =
        Retrofit.Builder().callFactory(okHttpCallFactory).addConverterFactory(
            Json.asConverterFactory("application/json".toMediaType())
        ).baseUrl(DATA_GO_KR_BASEURL).build().create(DataGoKrNetworkApi::class.java)


    @Provides
    @Singleton
    fun providesRecallSuspensionDataSource(
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher, dataGoKrNetworkApi: DataGoKrNetworkApi
    ): RecallSuspensionDataSource = RecallSuspensionDataSourceImpl(ioDispatcher, dataGoKrNetworkApi)

    @Provides
    @Singleton
    fun providesAdminActionDataSource(
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher, dataGoKrNetworkApi: DataGoKrNetworkApi
    ): AdminActionDataSource = AdminActionDataSourceImpl(ioDispatcher, dataGoKrNetworkApi)


    @Provides
    @Singleton
    fun provideMedicineApprovalDataSource(
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher, dataGoKrNetworkApi: DataGoKrNetworkApi
    ): MedicineApprovalDataSource = MedicineApprovalDataSourceImpl(ioDispatcher, dataGoKrNetworkApi)

}

interface DataGoKrNetworkApi {

    @GET(value = "DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnInq04")
    suspend fun getApprovalList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("item_name") itemName: String?,
        @Query("entp_name") entpName: String?,
        @Query("spclty_pblc") medicationType: String?,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): Response<MedicineApprovalListResponse>


    @GET(value = "DrugPrdtPrmsnInfoService04/getDrugPrdtPrmsnDtlInq03")
    suspend fun getMedicineDetailInfo(
        @Query("serviceKey", encoded = true) serviceKey: String = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("item_name") itemName: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = 100
    ): Response<MedicineDetailInfoResponse>

    /**
     * 의약품 회수·판매중지 목록 조회
     */
    @GET(value = "MdcinRtrvlSleStpgeInfoService03/getMdcinRtrvlSleStpgelList02")
    suspend fun getRecallSuspensionList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): Response<RecallSuspensionListResponse>

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
    ): Response<DetailRecallSuspensionResponse>

    /**
     * 행정 처분 목록조회
     */
    @GET(value = "MdcinExaathrService04/getMdcinExaathrList04")
    suspend fun getAdminActionList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("order") order: String = "Y",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): Response<AdminActionListResponse>
}