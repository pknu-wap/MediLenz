package com.android.mediproject.core.data.cache.repository

import com.android.mediproject.core.database.cache.DataCacheDao

abstract class DataCacheRepository(private val dao: DataCacheDao) {

    suspend fun delete(id: String) = dao.delete(id)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun isExist(id: String) = dao.isExist(id)

    suspend fun counts() = dao.counts()

}
