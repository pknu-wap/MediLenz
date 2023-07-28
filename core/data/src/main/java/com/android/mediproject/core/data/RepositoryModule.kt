package com.android.mediproject.core.data

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.adminaction.AdminActionRepository
import com.android.mediproject.core.data.adminaction.AdminActionRepositoryImpl
import com.android.mediproject.core.data.comments.CommentsRepository
import com.android.mediproject.core.data.comments.CommentsRepositoryImpl
import com.android.mediproject.core.data.dur.duringr.DurIngrRepository
import com.android.mediproject.core.data.dur.duringr.DurIngrRepositoryImpl
import com.android.mediproject.core.data.dur.durproduct.DurProductRepository
import com.android.mediproject.core.data.dur.durproduct.DurProductRepositoryImpl
import com.android.mediproject.core.data.favoritemedicine.FavoriteMedicineRepository
import com.android.mediproject.core.data.favoritemedicine.FavoriteMedicineRepositoryImpl
import com.android.mediproject.core.data.granule.GranuleIdentificationRepository
import com.android.mediproject.core.data.granule.GranuleIdentificationRepositoryImpl
import com.android.mediproject.core.data.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.medicineapproval.MedicineApprovalRepositoryImpl
import com.android.mediproject.core.data.medicineid.MedicineIdRepository
import com.android.mediproject.core.data.medicineid.MedicineIdRepositoryImpl
import com.android.mediproject.core.data.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.data.recallsuspension.RecallSuspensionRepositoryImpl
import com.android.mediproject.core.data.search.SearchHistoryRepository
import com.android.mediproject.core.data.search.SearchHistoryRepositoryImpl
import com.android.mediproject.core.data.sign.SignRepository
import com.android.mediproject.core.data.sign.SignRepositoryImpl
import com.android.mediproject.core.data.token.TokenRepository
import com.android.mediproject.core.data.token.TokenRepositoryImpl
import com.android.mediproject.core.data.user.UserInfoRepository
import com.android.mediproject.core.data.user.UserInfoRepositoryImpl
import com.android.mediproject.core.data.user.UserRepository
import com.android.mediproject.core.data.user.UserRepositoryImpl
import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.dur.DurIngrDataSource
import com.android.mediproject.core.network.datasource.dur.DurProductDataSource
import com.android.mediproject.core.network.datasource.favoritemedicine.FavoriteMedicineDataSource
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineid.MedicineIdDataSource
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionListDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionListDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
import com.android.mediproject.core.network.datasource.tokens.TokenDataSource
import com.android.mediproject.core.network.datasource.user.UserDataSource
import com.android.mediproject.core.network.datasource.user.UserInfoDataSource
import com.android.mediproject.core.network.tokens.TokenServer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource,
    ): UserRepository = UserRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideFavoriteMedicineRepository(favoriteMedicineDataSource: FavoriteMedicineDataSource): FavoriteMedicineRepository =
        FavoriteMedicineRepositoryImpl(favoriteMedicineDataSource)

    @Provides
    @Singleton
    fun provideMedicineApprovalRepository(
        medicineApprovalDataSource: MedicineApprovalDataSource,
        searchHistoryRepository: SearchHistoryRepository,
        @Dispatcher(MediDispatchers.Default) defaultDispatcher: CoroutineDispatcher,
        medicineDataCacheRepository: MedicineDataCacheManager,
    ): MedicineApprovalRepository =
        MedicineApprovalRepositoryImpl(medicineApprovalDataSource, searchHistoryRepository, defaultDispatcher, medicineDataCacheRepository)

    @Provides
    @Singleton
    fun provideRecallSuspensionRepository(
        recallSuspensionDataSource: RecallSuspensionDataSource, recallSuspensionListDataSource: RecallSuspensionListDataSourceImpl,
    ): RecallSuspensionRepository = RecallSuspensionRepositoryImpl(recallSuspensionDataSource, recallSuspensionListDataSource)

    @Provides
    @Singleton
    fun providesAdminActionRepository(
        adminActionDataSource: AdminActionListDataSourceImpl,
    ): AdminActionRepository = AdminActionRepositoryImpl(adminActionDataSource)

    @Provides
    @Singleton
    fun providesGranuleIdentificationRepository(
        granuleIdentificationDataSource: GranuleIdentificationDataSource,
    ): GranuleIdentificationRepository = GranuleIdentificationRepositoryImpl(granuleIdentificationDataSource)

    @Provides
    @Singleton
    fun providesDurProductRepository(durProductDataSource: DurProductDataSource): DurProductRepository =
        DurProductRepositoryImpl(durProductDataSource)

    @Provides
    @Singleton
    fun providesDurIngrRepository(durIngrDataSource: DurIngrDataSource): DurIngrRepository = DurIngrRepositoryImpl(durIngrDataSource)

    @Provides
    @Singleton
    fun providesCommentsRepository(commentsDataSource: CommentsDataSource, tokenRepository: TokenRepository): CommentsRepository =
        CommentsRepositoryImpl(commentsDataSource, tokenRepository)


    @Provides
    @Singleton
    fun providesSignRepository(
        signDataSource: SignDataSource,
        appDataStore: AppDataStore,
        userInfoRepository: UserInfoRepository,
    ): SignRepository = SignRepositoryImpl(signDataSource, appDataStore, userInfoRepository)

    @Provides
    @Singleton
    fun providesSearchHistoryRepository(
        searchHistoryDao: SearchHistoryDao,
    ): SearchHistoryRepository = SearchHistoryRepositoryImpl(searchHistoryDao)

    @Provides
    @Singleton
    fun providesMedicineIdRepository(
        medicineIdDataSource: MedicineIdDataSource,
    ): MedicineIdRepository = MedicineIdRepositoryImpl(medicineIdDataSource)


    @Provides
    @Singleton
    fun providesUserInfoRepository(
        userInfoDataSource: UserInfoDataSource, appDataStore: AppDataStore,
    ): UserInfoRepository = UserInfoRepositoryImpl(userInfoDataSource, appDataStore)

    @Provides
    @Singleton
    fun providesTokenRepository(
        tokenDataSource: TokenDataSource,
        tokenServer: TokenServer,
    ): TokenRepository = TokenRepositoryImpl(tokenDataSource, tokenServer)
}
