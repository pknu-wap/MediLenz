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

    private val ADDITIONAL_QUERY = "의약품+"

    // url 캐시
    private val urlCache = LruCache<String, String>(50)

    override suspend fun getImageUrl(medicineName: String): Result<String> {
        val query = ADDITIONAL_QUERY + medicineName

        return synchronized(urlCache) {
            urlCache.get(query)
        }?.let {
            Result.success(it)
        } ?: run {
            val response = googleSearchNetworkApi.getImageUrl(query)
            response.onResponse().fold(
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
