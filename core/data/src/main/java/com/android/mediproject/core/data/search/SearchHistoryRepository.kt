package com.android.mediproject.core.data.search

import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun insertSearchHistory(query: String)

    suspend fun getSearchHistoryList(count: Int = 5): Flow<List<SearchHistoryDto>>

    suspend fun deleteSearchHistory(id: Long)

    suspend fun deleteAllSearchHistory()
}