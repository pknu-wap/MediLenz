package com.android.mediproject.core.database.searchhistory

fun SearchHistoryDto.toSearchHistoryItemDto(): com.android.mediproject.core.model.search.local.SearchHistoryItemDto {
    return com.android.mediproject.core.model.search.local.SearchHistoryItemDto(
        id = id, query = query, searchedAt = searchDateTime,
    )
}
