package com.android.mediproject.core.network.datasource.image

import android.util.LruCache
import com.android.mediproject.core.network.module.GoogleSearchNetworkApi
import com.android.mediproject.core.network.onResponse
import com.android.mediproject.core.network.parser.HtmlParser
import javax.inject.Inject

class GoogleSearchDataSourceImpl @Inject constructor(
    private val googleSearchNetworkApi: GoogleSearchNetworkApi,
    private val htmlParser: HtmlParser,
) : GoogleSearchDataSource {

    private val additionalQuery = "의약품 "

    private val urlCache = LruCache<String, String>(60)

    override suspend fun getImageUrl(medicineName: String): Result<String> {
        val query = additionalQuery + medicineName

        return synchronized(urlCache) {
            urlCache.get(query)
        }?.let {
            Result.success(it)
        } ?: run {
            googleSearchNetworkApi.getImageUrl(query).onResponse().fold(
                onSuccess = {
                    val url = htmlParser.parse(medicineName, it)
                    synchronized(urlCache) {
                        urlCache.put(query, url)
                    }
                    Result.success(url)
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        }
    }

}
