package com.android.mediproject.core.data.remote.di

import com.android.mediproject.core.data.remote.adminaction.AdminActionRepository
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepositoryImpl
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepositoryImpl
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepository
import com.android.mediproject.core.data.remote.recallsuspension.RecallSuspensionRepositoryImpl
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.penalties.AdminActionDataSourceImpl
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionDataSource
import com.android.mediproject.core.network.datasource.penalties.RecallSuspensionListDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMedicineApprovalRepository(
        medicineApprovalDataSource: MedicineApprovalDataSource
    ): MedicineApprovalRepository = MedicineApprovalRepositoryImpl(medicineApprovalDataSource)

    @Provides
    @Singleton
    fun provideRecallSuspensionRepository(
        recallSuspensionDataSource: RecallSuspensionDataSource, recallSuspensionListDataSource: RecallSuspensionListDataSourceImpl
    ): RecallSuspensionRepository = RecallSuspensionRepositoryImpl(recallSuspensionDataSource, recallSuspensionListDataSource)

    @Provides
    @Singleton
    fun providesAdminActionRepository(
        adminActionDataSource: AdminActionDataSourceImpl
    ): AdminActionRepository = AdminActionRepositoryImpl(adminActionDataSource)
    
}