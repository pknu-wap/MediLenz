package com.android.mediproject.core.network.datasource.news.recallsuspension

import com.android.mediproject.core.model.news.recall.DetailRecallSaleSuspensionResponse
import com.android.mediproject.core.network.datasource.image.GoogleSearchDataSource
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RecallSaleSuspensionDataSourceImpl @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
    private val googleSearchDataSource: GoogleSearchDataSource,
) : RecallSaleSuspensionDataSource {


    override suspend fun getDetailRecallSaleSuspension(
        company: String?, product: String?,
    ): Result<DetailRecallSaleSuspensionResponse> =
        dataGoKrNetworkApi.getDetailRecallSuspensionInfo(company = company, product = product).onDataGokrResponse().fold(
            onSuccess = {
                val imageMap = googleSearchDataSource.fetchImageUrls(
                    it.body.items.map { item ->
                        item.item.product
                    },
                    "의약품 ",
                )
                it.body.items.map { item ->
                    item.item.imageUrl = imageMap[item.item.product]!!
                }
                Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            },
        )


    override suspend fun getRecallSaleSuspensionList(pageNo: Int, numOfRows: Int) =
        dataGoKrNetworkApi.getRecallSuspensionList(pageNo = pageNo, numOfRows = numOfRows).onDataGokrResponse().map {
            it.body.items.let { items ->
                val imageMap = googleSearchDataSource.fetchImageUrls(
                    items.map { item ->
                        item.item.product
                    },
                    "의약품 ",
                )
                items.map { item ->
                    item.item.imageUrl = imageMap[item.item.product]!!
                }
                it
            }
        }
}
