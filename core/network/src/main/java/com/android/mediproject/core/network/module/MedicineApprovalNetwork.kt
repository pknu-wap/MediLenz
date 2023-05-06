package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.BuildConfig
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.remote.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MedicineApprovalNetwork {
    @Provides
    @Singleton
    fun provideMedicineApprovalNetworkApi(retrofitBuilder: Retrofit.Builder): MedicineApprovalNetworkApi =
        retrofitBuilder.baseUrl(DATAGOKR_BASE_URL).build().create(MedicineApprovalNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideMedicineApprovalDataSource(
        @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher, medicineApprovalNetworkApi: MedicineApprovalNetworkApi
    ): MedicineApprovalDataSource = MedicineApprovalDataSourceImpl(ioDispatcher, medicineApprovalNetworkApi)

}

// 의약품 제품 허가정보 URL
private const val DATAGOKR_BASE_URL = "https://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService04/"

private const val APPROVAL_LIST_URL = "getDrugPrdtPrmsnInq04"
private const val APPROVAL_DETAIL_URL = "getDrugPrdtPrmsnDtlInq03"

interface MedicineApprovalNetworkApi {

    @GET(value = APPROVAL_LIST_URL)
    suspend fun getApprovalList(
        @Query("serviceKey", encoded = true) serviceKey: String? = BuildConfig.DATA_GO_KR_SERVICE_KEY,
        @Query("item_name") itemName: String?,
        @Query("entp_name") entpName: String?,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String = "json",
        @Query("numOfRows") numOfRows: Int = DATA_GO_KR_PAGE_SIZE
    ): MedicineApprovalListResponse
}