package com.android.mediproject.core.database.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MedicineCacheDao : DataCacheDao {

    @Query("SELECT * FROM medicine_detail_cache_table WHERE itemSeq = :itemSeq")
    suspend fun select(itemSeq: String): MedicineCacheDto

    @Query("DELETE FROM medicine_detail_cache_table WHERE itemSeq = :id")
    override suspend fun delete(id: String)

    @Query("DELETE FROM medicine_detail_cache_table")
    override suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT * FROM medicine_detail_cache_table WHERE itemSeq = :id)")
    override suspend fun isExist(id: String): Boolean

    @Query("SELECT COUNT(*) FROM medicine_detail_cache_table")
    override suspend fun counts(): Int

    @Query("SELECT * FROM medicine_detail_cache_table")
    suspend fun selectAll(): List<MedicineCacheDto>

    @Query("SELECT * FROM medicine_detail_cache_table WHERE itemSeq IN (:itemSeqs)")
    suspend fun selectAll(itemSeqs: List<String>): List<MedicineCacheDto>

    @Query("SELECT itemSeq, image_url FROM medicine_detail_cache_table WHERE itemSeq IN (:itemSeqs)")
    suspend fun selectAllImageUrls(itemSeqs: List<String>): List<MedicineCacheDto>

    @Upsert
    suspend fun update(medicineCacheDtos: List<MedicineCacheDto>)
}
