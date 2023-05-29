package com.android.mediproject.core.database.searchhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history_table")
data class SearchHistoryDto(
    @PrimaryKey(autoGenerate = true) val id: Long,

    @ColumnInfo(name = "query") val query: String, @ColumnInfo(name = "search_date_time") val searchDateTime: String
)