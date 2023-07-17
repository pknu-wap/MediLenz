package com.android.mediproject.core.data.cache.repository

import com.android.mediproject.core.database.cache.DataCacheDao
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity

abstract class MedicineDataCacheRepository(dao: DataCacheDao) : DataCacheRepository(dao) {
    abstract suspend fun selectAll(): List<MedicineCacheEntity>

    abstract suspend fun selectAll(ids: List<String>): List<MedicineCacheEntity>

    abstract suspend fun selectAllImageUrls(ids: List<String>): List<MedicineCacheEntity>

    abstract suspend fun updates(entities: List<MedicineCacheEntity>)

    abstract suspend fun select(id: String): MedicineCacheEntity
}
