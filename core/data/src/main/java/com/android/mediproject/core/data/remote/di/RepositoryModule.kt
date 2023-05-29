package com.android.mediproject.core.data.remote.di

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepository
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepositoryImpl
import com.android.mediproject.core.data.remote.comments.CommentsRepository
import com.android.mediproject.core.data.remote.comments.CommentsRepositoryImpl
import com.android.mediproject.core.data.remote.dur.DurRepository
import com.android.mediproject.core.data.remote.dur.DurRepositoryImpl
import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepository
import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepositoryImpl
import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepository
import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepositoryImpl
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepositoryImpl
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepositoryImpl
import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.data.remote.sign.SignRepositoryImpl
import com.android.mediproject.core.data.search.SearchHistoryRepository
import com.android.mediproject.core.data.search.SearchHistoryRepositoryImpl
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.datastore.AppDataStore
import com.android.mediproject.core.datastore.TokenDataSourceImpl
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.dur.DurDataSource
import com.android.mediproject.core.network.datasource.elderlycaution.ElderlyCautionDataSource
import com.android.mediproject.core.network.datasource.granule.GranuleIdentificationDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.penalties.adminaction.AdminActionListDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.recallsuspension.RecallSuspensionListDataSourceImpl
import com.android.mediproject.core.network.datasource.sign.SignDataSource
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
    fun provideMedicineApprovalRepository(
        medicineApprovalDataSource: MedicineApprovalDataSource, searchHistoryDao: SearchHistoryDao
    ): MedicineApprovalRepository = MedicineApprovalRepositoryImpl(
        medicineApprovalDataSource, searchHistoryDao
    )

    @Provides
    @Singleton
    fun provideRecallSuspensionRepository(
        recallSuspensionDataSource: RecallSuspensionDataSource, recallSuspensionListDataSource: RecallSuspensionListDataSourceImpl
    ): RecallSuspensionRepository = RecallSuspensionRepositoryImpl(recallSuspensionDataSource, recallSuspensionListDataSource)

    @Provides
    @Singleton
    fun providesAdminActionRepository(
        adminActionDataSource: AdminActionListDataSourceImpl
    ): AdminActionRepository = AdminActionRepositoryImpl(adminActionDataSource)

    @Provides
    @Singleton
    fun providesGranuleIdentificationRepository(
        granuleIdentificationDataSource: GranuleIdentificationDataSource
    ): GranuleIdentificationRepository = GranuleIdentificationRepositoryImpl(granuleIdentificationDataSource)

    @Provides
    @Singleton
    fun providesElderlyCautionRepository(elderlyCautionDataSource: ElderlyCautionDataSource): ElderlyCautionRepository =
        ElderlyCautionRepositoryImpl(elderlyCautionDataSource)

    @Provides
    @Singleton
    fun providesDurRepository(durDataSource: DurDataSource): DurRepository = DurRepositoryImpl(durDataSource)


    @Provides
    @Singleton
    fun providesCommentsRepository(commentsDataSource: CommentsDataSource): CommentsRepository = CommentsRepositoryImpl(commentsDataSource)


    @Provides
    @Singleton
    fun providesSignRepository(
        signDataSource: SignDataSource, connectionTokenDataSourceImpl: TokenDataSourceImpl, appDataStore: AppDataStore
    ): SignRepository = SignRepositoryImpl(signDataSource, connectionTokenDataSourceImpl, appDataStore)

    @Provides
    @Singleton
    fun providesSearchHistoryRepository(
        searchHistoryDao: SearchHistoryDao, @Dispatcher(MediDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): SearchHistoryRepository = SearchHistoryRepositoryImpl(searchHistoryDao, ioDispatcher)
}