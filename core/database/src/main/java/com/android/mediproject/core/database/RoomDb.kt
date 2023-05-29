package com.android.mediproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto


@Database(entities = [SearchHistoryDto::class], version = 1)
abstract class RoomDb : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}