package com.android.mediproject.core.data.search

import androidx.annotation.WorkerThread
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    @WorkerThread
    suspend fun insertSearchHistory(searchHistoryDto: SearchHistoryDto)

    @WorkerThread

    fun getSearchHistoryList(count: Int = 5): Flow<List<SearchHistoryDto>>

    @WorkerThread

    suspend fun deleteSearchHistory(id: Long)

    @WorkerThread

    suspend fun deleteAllSearchHistory()
}
