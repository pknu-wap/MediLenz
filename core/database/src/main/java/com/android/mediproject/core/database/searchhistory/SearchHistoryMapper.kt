package com.android.mediproject.core.database.searchhistory

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun SearchHistoryDto.toSearchHistoryItemDto(): com.android.mediproject.core.model.search.local.SearchHistoryItemDto {
    return com.android.mediproject.core.model.search.local.SearchHistoryItemDto(
        id = id, query = query, searchedAt = LocalDateTime.parse(this.searchDateTime, dateTimeFormatter)
    )
}

private val dateTimeFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") }