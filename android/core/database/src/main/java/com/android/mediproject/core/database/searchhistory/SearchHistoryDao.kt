package com.android.mediproject.core.database.searchhistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SearchHistoryDao {

    /**
     * 검색 기록을 추가한다.
     * @param searchHistoryDto
     */
    @Transaction
    open suspend fun insert(SearchHistoryDto: SearchHistoryDto) {
        if (!isExist(SearchHistoryDto.query)) realInsert(SearchHistoryDto)
    }

    @Insert(entity = SearchHistoryDto::class, onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun realInsert(SearchHistoryDto: SearchHistoryDto)

    /**
     * id에 해당하는 검색 기록을 삭제한다.
     * @param id
     */
    @Query("DELETE FROM search_history_table WHERE id = :id")
    abstract suspend fun delete(id: Long)

    /**
     * 개수 만큼 검색 목록을 가져온다.
     * @param count
     */
    @Query("SELECT * FROM search_history_table ORDER BY id DESC LIMIT :count")
    abstract fun select(count: Int): Flow<List<SearchHistoryDto>>

    /**
     * 모든 검색 기록을 삭제한다.
     */
    @Query("DELETE FROM search_history_table")
    abstract suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT * FROM search_history_table WHERE `query` = :query)")
    abstract suspend fun isExist(query: String): Boolean
}