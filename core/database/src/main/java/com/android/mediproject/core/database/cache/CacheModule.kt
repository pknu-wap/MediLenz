package com.android.mediproject.core.database.cache

import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManagerImpl
import com.android.mediproject.core.database.zip.Zipper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Provides
    @Singleton
    fun providesMedicineCacheManager(
        medicineDetailCacheDao: MedicineDetailCacheDao,
        medicineImageCacheDao: MedicineImageCacheDao,
        zipper: Zipper,
    ): MedicineDataCacheManager = MedicineDataCacheManagerImpl(medicineDetailCacheDao, medicineImageCacheDao, zipper)
}
