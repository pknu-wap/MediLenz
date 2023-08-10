package com.android.mediproject.core.network.datasource.image

import android.util.LruCache
import com.android.mediproject.core.network.module.GoogleSearchNetworkApi
import com.android.mediproject.core.network.module.safetyEncode
import com.android.mediproject.core.network.parser.HtmlParser
import com.android.mediproject.core.network.retrofitext.NetworkApiResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class GoogleSearchDataSourceImpl @Inject constructor(
    private val googleSearchNetworkApi: GoogleSearchNetworkApi,
    private val htmlParser: HtmlParser,
) : GoogleSearchDataSource {

    private val urlCache = LruCache<String, String>(100)
    private val mutex = Mutex()

    @OptIn(DelicateCoroutinesApi::class) private val threads = newFixedThreadPoolContext(3, "GoogleSearchProcessor")

    private suspend fun getImageUrl(query: String, additionalQuery: String): Result<String> {
        val finalQuery = (additionalQuery + query).safetyEncode()

        return mutex.withLock {
            urlCache.get(finalQuery)
        }?.run {
            Result.success(this)
        } ?: run {
            when (val result = googleSearchNetworkApi.getImageUrl(query = finalQuery)) {
                is NetworkApiResult.Success -> {
                    val imageUrl = htmlParser.parse(finalQuery, result.data)
                    mutex.withLock {
                        urlCache.put(finalQuery, imageUrl)
                    }
                    Result.success(imageUrl)
                }

                is NetworkApiResult.Failure -> {
                    Result.failure(result.exception)
                }
            }
        }
    }

    override suspend fun fetchImageUrls(elements: List<String>, additionalQuery: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val requests = elements.map { query ->
            supervisorScope {
                async(threads) {
                    val imageUrl = getImageUrl(query, additionalQuery)
                    synchronized(map) {
                        map[query] = imageUrl.getOrDefault("")
                    }
                }
            }
        }

        requests.forEach { it.await() }
        return map
    }
}
