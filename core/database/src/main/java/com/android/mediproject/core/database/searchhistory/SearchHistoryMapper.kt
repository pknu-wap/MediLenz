package com.android.mediproject.core.database.searchhistory

import java.time.LocalDateTime

fun SearchHistoryDto.toSearchHistoryItemDto(): com.android.mediproject.core.model.search.local.SearchHistoryItemDto {
    return com.android.mediproject.core.model.search.local.SearchHistoryItemDto(
        id = id, query = query, searchedAt = LocalDateTime.parse(this.searchDateTime)
    )
}