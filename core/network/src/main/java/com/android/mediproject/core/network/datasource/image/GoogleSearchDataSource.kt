package com.android.mediproject.core.network.datasource.image

interface GoogleSearchDataSource {
    suspend fun fetchImageUrls(elements: List<String>, additionalQuery: String = ""): Map<String, String>

}
