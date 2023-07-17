package com.android.mediproject.core.network.datasource.image

import com.android.mediproject.core.network.module.GoogleSearchNetworkApi
import com.android.mediproject.core.network.onResponse
import com.android.mediproject.core.network.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleImageDownloadDataSourceImpl @Inject constructor(
    private val googleSearchNetworkApi: GoogleSearchNetworkApi,
    private val htmlParser: HtmlParser,
    private val defaultDispatcher: CoroutineDispatcher,
) {

    fun getImageUrl(medicineName: String): Flow<Result<String>> = channelFlow {
        val response = googleSearchNetworkApi.getImageUrl(medicineName)
        withContext(defaultDispatcher) {
            response.onResponse().fold(
                onSuccess = { response ->
                    send(Result.success(htmlParser.parse(response)))
                },
                onFailure = {
                    send(Result.failure(it))
                },
            )
        }
    }
}
