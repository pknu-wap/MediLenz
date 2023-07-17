package com.android.mediproject.core.database.cache

interface DataCacheDao {
    suspend fun delete(id: String)

    suspend fun deleteAll()

    suspend fun isExist(id: String): Boolean

    suspend fun counts(): Int
}
