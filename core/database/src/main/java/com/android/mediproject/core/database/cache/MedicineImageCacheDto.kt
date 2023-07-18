package com.android.mediproject.core.database.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_image_cache_table")
data class MedicineImageCacheDto(
    @ColumnInfo(name = "item_seq")
    @PrimaryKey(autoGenerate = false)
    val itemSeq: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)
