package com.android.mediproject.core.model.search.local

import java.time.LocalDateTime

data class SearchHistoryItemDto(
    val id: Long,
    val query: String,
    val searchedAt: LocalDateTime
)