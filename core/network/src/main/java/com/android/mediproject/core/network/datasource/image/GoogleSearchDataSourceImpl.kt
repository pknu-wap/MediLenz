package com.android.mediproject.core.network.datasource.image

import com.android.mediproject.core.network.module.GoogleSearchNetworkApi
import com.android.mediproject.core.network.onResponse
import com.android.mediproject.core.network.parser.HtmlParser
import javax.inject.Inject

class GoogleSearchDataSourceImpl @Inject constructor(
    private val googleSearchNetworkApi: GoogleSearchNetworkApi,
    private val htmlParser: HtmlParser,
) : GoogleSearchDataSource {

    override suspend fun getImageUrl(medicineName: String): Result<String> {
        val response = googleSearchNetworkApi.getImageUrl(medicineName)
        return response.onResponse().fold(
            onSuccess = { response ->
                Result.success(htmlParser.parse(response))
            },
            onFailure = {
                Result.failure(it)
            },
        )
    }
}
