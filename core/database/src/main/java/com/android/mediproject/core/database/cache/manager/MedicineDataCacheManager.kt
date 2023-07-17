package com.android.mediproject.core.database.cache.manager

import com.android.mediproject.core.database.cache.DataCacheDao
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity

abstract class MedicineDataCacheManager(dao: DataCacheDao) : DataCacheManager(dao) {
    abstract suspend fun select(ids: List<String>): List<MedicineCacheEntity>

    abstract suspend fun selectWithImageUrls(ids: List<String>): List<MedicineCacheEntity>

    abstract fun updateDetail(entity: MedicineCacheEntity)
    abstract fun updateImage(entity: MedicineCacheEntity)

    abstract suspend fun select(id: String): MedicineCacheEntity

}
