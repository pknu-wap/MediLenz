package com.android.mediproject.core.database.searchhistory

import com.android.mediproject.core.model.search.SearchHistory

fun SearchHistoryDto.toSearchHistoryItemDto(): SearchHistory {
    return SearchHistory(
        id = id, query = query, searchedAt = searchDateTime,
    )
}
