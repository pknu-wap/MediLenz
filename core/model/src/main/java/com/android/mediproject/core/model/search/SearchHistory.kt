package com.android.mediproject.core.model.search

import java.time.LocalDateTime

data class SearchHistory(
    val id: Long,
    val query: String,
    val searchedAt: LocalDateTime,
)
