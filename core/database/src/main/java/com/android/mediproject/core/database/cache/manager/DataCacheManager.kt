package com.android.mediproject.core.database.cache.manager

import com.android.mediproject.core.database.cache.DataCacheDao

abstract class DataCacheManager(private val dao: DataCacheDao) {

    open suspend fun delete(id: String) = dao.delete(id)

    open suspend fun deleteAll() = dao.deleteAll()

    open suspend fun isExist(id: String) = dao.isExist(id)

    open suspend fun count() = dao.count()

}
