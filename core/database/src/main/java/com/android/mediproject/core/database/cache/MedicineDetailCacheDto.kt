package com.android.mediproject.core.database.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_detail_cache_table")
data class MedicineDetailCacheDto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "item_seq")
    val itemSeq: String,
    @ColumnInfo(name = "data") val data: ByteArray,
    @ColumnInfo(name = "change_date") val changeDate: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicineDetailCacheDto

        if (itemSeq != other.itemSeq) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemSeq.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
