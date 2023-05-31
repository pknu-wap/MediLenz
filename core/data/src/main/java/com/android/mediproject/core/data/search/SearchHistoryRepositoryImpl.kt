package com.android.mediproject.core.data.search

import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {
    override fun insertSearchHistory(searchHistoryDto: SearchHistoryDto) = runBlocking {
        searchHistoryDao.insert(searchHistoryDto.apply
        {
            query = query.trim()
        })
    }

    override fun getSearchHistoryList(count: Int): Flow<List<SearchHistoryDto>> = searchHistoryDao.select(count)


    override suspend fun deleteSearchHistory(id: Long) {
        searchHistoryDao.delete(id)
    }


    override suspend fun deleteAllSearchHistory() {
        searchHistoryDao.deleteAll()
    }


}