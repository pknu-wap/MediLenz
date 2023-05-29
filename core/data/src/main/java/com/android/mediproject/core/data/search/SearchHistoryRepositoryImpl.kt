package com.android.mediproject.core.data.search

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.database.searchhistory.SearchHistoryDao
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao, @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : SearchHistoryRepository {
    override suspend fun insertSearchHistory(query: String) {
        withContext(ioDispatcher) {
            searchHistoryDao.insert(
                SearchHistoryDto(
                    query = query, id = 0L, searchDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
                )
            )
        }
    }

    override suspend fun getSearchHistoryList(count: Int): Flow<List<SearchHistoryDto>> = withContext(ioDispatcher) {
        searchHistoryDao.select(count)
    }


    override suspend fun deleteSearchHistory(id: Long) {
        withContext(ioDispatcher) {
            searchHistoryDao.delete(id)
        }
    }

    override suspend fun deleteAllSearchHistory() {
        withContext(ioDispatcher) {
            searchHistoryDao.deleteAll()
        }
    }

}