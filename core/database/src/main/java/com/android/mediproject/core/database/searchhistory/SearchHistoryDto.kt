package com.android.mediproject.core.database.searchhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "search_history_table")
data class SearchHistoryDto(
    @PrimaryKey(autoGenerate = true) val id: Long,

    @ColumnInfo(name = "query") var query: String,

    @ColumnInfo(
        name = "search_date_time"
    ) val searchDateTime: LocalDateTime

) {
    constructor(query: String) : this(0, query, LocalDateTime.now())

}