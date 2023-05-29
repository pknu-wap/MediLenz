package com.android.mediproject.core.database.searchhistory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    /**
     * 검색 기록을 추가한다.
     * @param searchHistoryDto
     */
    @Upsert
    suspend fun insert(searchHistoryDto: SearchHistoryDto)

    /**
     * id에 해당하는 검색 기록을 삭제한다.
     * @param id
     */
    @Delete
    suspend fun delete(id: Long)

    /**
     * 개수 만큼 검색 목록을 가져온다.
     * @param count
     */
    @Query("SELECT * FROM search_history_table ORDER BY id DESC LIMIT :count")
    suspend fun select(count: Int): Flow<List<SearchHistoryDto>>

    /**
     * 모든 검색 기록을 삭제한다.
     */
    @Query("DELETE FROM search_history_table")
    @Delete
    suspend fun deleteAll()
}