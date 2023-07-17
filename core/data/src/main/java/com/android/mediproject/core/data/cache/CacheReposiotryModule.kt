package com.android.mediproject.core.data.cache

import com.android.mediproject.core.data.cache.repository.MedicineDataCacheRepository
import com.android.mediproject.core.data.cache.repository.MedicineDataCacheRepositoryImpl
import com.android.mediproject.core.database.cache.MedicineCacheDao
import com.android.mediproject.core.database.zip.Zipper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheReposiotryModule {

    @Provides
    @Singleton
    fun providesMedicineCacheRepository(
        medicineCacheDao: MedicineCacheDao,
        zipper: Zipper,
    ): MedicineDataCacheRepository = MedicineDataCacheRepositoryImpl(medicineCacheDao, zipper)
}
