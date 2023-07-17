package com.android.mediproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.mediproject.core.database.cache.MedicineDetailCacheDao
import com.android.mediproject.core.database.cache.MedicineDetailCacheDto
import com.android.mediproject.core.database.cache.MedicineImageCacheDao
import com.android.mediproject.core.database.cache.MedicineImageCacheDto
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto


@Database(entities = [SearchHistoryDto::class, MedicineDetailCacheDto::class, MedicineImageCacheDto::class], version = 1, exportSchema = true)
@TypeConverters(RoomTypeConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun medicineDetailCacheDao(): MedicineDetailCacheDao
    abstract fun medicineImageCacheDao(): MedicineImageCacheDao
}
