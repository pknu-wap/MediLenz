package com.android.mediproject.core.database.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MedicineImageCacheDao : DataCacheDao {

    @Query("SELECT * FROM medicine_image_cache_table WHERE item_seq = :itemSeq")
    suspend fun select(itemSeq: String): MedicineImageCacheDto

    @Query("DELETE FROM medicine_image_cache_table WHERE item_seq = :id")
    override suspend fun delete(id: String)

    @Query("DELETE FROM medicine_image_cache_table")
    override suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT * FROM medicine_image_cache_table WHERE item_seq = :id)")
    override suspend fun isExist(id: String): Boolean

    @Query("SELECT COUNT(*) FROM medicine_image_cache_table")
    override suspend fun counts(): Int

    @Query("SELECT * FROM medicine_image_cache_table")
    suspend fun selectAll(): List<MedicineImageCacheDto>

    @Query("SELECT * FROM medicine_image_cache_table WHERE item_seq IN (:itemSeqs)")
    suspend fun selectAll(itemSeqs: List<String>): List<MedicineImageCacheDto>

    @Upsert
    suspend fun update(medicineCacheDto: MedicineImageCacheDto)
}
