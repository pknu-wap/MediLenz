package com.android.mediproject.core.model.searchmedicines

import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(var query: String? = null)