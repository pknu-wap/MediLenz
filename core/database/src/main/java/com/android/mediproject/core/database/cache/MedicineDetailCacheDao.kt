package com.android.mediproject.core.database.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.android.mediproject.core.database.cache.join.MedicineCacheJoinResult

@Dao
interface MedicineDetailCacheDao : DataCacheDao {

    @Query("SELECT * FROM medicine_detail_cache_table WHERE item_seq = :itemSeq")
    suspend fun select(itemSeq: String): MedicineDetailCacheDto

    @Query("DELETE FROM medicine_detail_cache_table WHERE item_seq = :id")
    override suspend fun delete(id: String)

    @Query("DELETE FROM medicine_detail_cache_table")
    override suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT * FROM medicine_detail_cache_table WHERE item_seq = :id)")
    override suspend fun isExist(id: String): Boolean

    @Query("SELECT COUNT(*) FROM medicine_detail_cache_table")
    override suspend fun count(): Int


    @Query("SELECT * FROM medicine_detail_cache_table as detail INNER JOIN medicine_image_cache_table as image ON detail.item_seq = image.item_seq WHERE detail.item_seq IN (:itemSeqs)")
    suspend fun selectWithImages(itemSeqs: List<String>): List<MedicineCacheJoinResult>

    @Query("SELECT * FROM medicine_detail_cache_table WHERE item_seq IN (:itemSeqs)")
    suspend fun select(itemSeqs: List<String>): List<MedicineDetailCacheDto>

    @Upsert
    suspend fun update(medicineDetailCacheDto: MedicineDetailCacheDto)

    @Query("SELECT change_date FROM medicine_detail_cache_table WHERE item_seq = :itemSeq")
    suspend fun selectChangeDate(itemSeq: String): String
}
