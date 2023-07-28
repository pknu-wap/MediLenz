package com.android.mediproject.core.network.module

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.network.datasource.dur.DurIngrDataSource
import com.android.mediproject.core.network.datasource.dur.DurIngrDataSourceImpl
import com.android.mediproject.core.network.datasource.dur.DurProductDataSource
import com.android.mediproject.core.network.datasource.dur.DurProductDataSourceImpl
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSourceImpl
import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionDataSource
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSourceImpl
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.module.datagokr.DurIngrInfoNetworkApi
import com.android.mediproject.core.network.module.datagokr.DurProductInfoNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val DATA_GO_KR_BASEURL = "https://apis.data.go.kr/1471000/"

@InstallIn(SingletonComponent::class)
@Module
object DataGoKrNetwork {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // Retrofit2 인스턴스 ---------------------------------------------------------------------------------------------------
    @Provides
    @Singleton
    @Named("dataGoKrNetworkApiWithJsonResponse")
    fun providesDataGoKrNetworkApiWithJson(okHttpClient: OkHttpClient): DataGoKrNetworkApi =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(DATA_GO_KR_BASEURL).build().create(DataGoKrNetworkApi::class.java)

    @Provides
    @Singleton
    @Named("dataGoKrNetworkApiWithStringResponse")
    fun providesDataGoKrNetworkApiWithString(okHttpClient: OkHttpClient): DataGoKrNetworkApi =
        Retrofit.Builder().client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create()).baseUrl(DATA_GO_KR_BASEURL).build()
            .create(DataGoKrNetworkApi::class.java)

    @Provides
    @Singleton
    fun providesDurProductInfoNetworkApi(
        @Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApi: DataGoKrNetworkApi,
    ): DurProductInfoNetworkApi = dataGoKrNetworkApi

    @Provides
    @Singleton
    fun providesDurIngrInfoNetworkApi(@Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApi: DataGoKrNetworkApi): DurIngrInfoNetworkApi =
        dataGoKrNetworkApi

    // DataSource 인스턴스 ---------------------------------------------------------------------------------------------------

    @Provides
    @Singleton
    fun providesRecallSuspensionDataSource(
        @Dispatcher(MediDispatchers.Default) defaultDispatcher: CoroutineDispatcher,
        @Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApi: DataGoKrNetworkApi,
    ): RecallSuspensionDataSource = RecallSuspensionDataSourceImpl(defaultDispatcher, dataGoKrNetworkApi)

    @Provides
    @Singleton
    fun providesAdminActionDataSource(
        @Dispatcher(MediDispatchers.Default) defaultDispatcher: CoroutineDispatcher,
        @Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApi: DataGoKrNetworkApi,
    ): AdminActionDataSource = AdminActionDataSourceImpl(defaultDispatcher, dataGoKrNetworkApi)


    @Provides
    @Singleton
    fun provideMedicineApprovalDataSource(
        @Named("dataGoKrNetworkApiWithStringResponse") dataGoKrNetworkApiWithString: DataGoKrNetworkApi,
        @Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApiWithJson: DataGoKrNetworkApi,
        medicineDataCacheManager: MedicineDataCacheManager,
        googleSearchDataSource: GoogleSearchDataSource,
        @Dispatcher(MediDispatchers.Default) defaultDispatcher: CoroutineDispatcher,
    ): MedicineApprovalDataSource = MedicineApprovalDataSourceImpl(
        dataGoKrNetworkApiWithString, dataGoKrNetworkApiWithJson, medicineDataCacheManager, googleSearchDataSource, defaultDispatcher,
    )

    @Provides
    @Singleton
    fun providesGranuleIdentificationDataSource(
        @Named("dataGoKrNetworkApiWithJsonResponse") dataGoKrNetworkApi: DataGoKrNetworkApi,
    ): GranuleIdentificationDataSource = GranuleIdentificationDataSourceImpl(dataGoKrNetworkApi)

    // DUR 품목 정보 ---------------------------------------------------------------------------------------------------
    @Provides
    @Singleton
    fun providesDurProductDataSource(api: DurProductInfoNetworkApi): DurProductDataSource = DurProductDataSourceImpl(api)

    @Provides
    @Singleton
    fun providesDurIngrDataSource(api: DurIngrInfoNetworkApi): DurIngrDataSource = DurIngrDataSourceImpl(api)
}
