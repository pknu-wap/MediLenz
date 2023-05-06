package com.android.mediproject.core.data.remote.di

import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepositoryImpl
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
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

}