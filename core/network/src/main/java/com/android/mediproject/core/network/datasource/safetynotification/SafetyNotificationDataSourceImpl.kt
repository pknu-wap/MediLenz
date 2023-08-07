package com.android.mediproject.core.network.datasource.safetynotification

import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.medicine.safetynotification.SafetyNotificationResponse
import com.android.mediproject.core.network.module.datagokr.DataGoKrNetworkApi
import com.android.mediproject.core.network.onDataGokrResponse
import javax.inject.Inject

class SafetyNotificationDataSourceImpl @Inject constructor(
    private val dataGoKrNetworkApi: DataGoKrNetworkApi,
) : SafetyNotificationDataSource() {

    override fun getRefreshKey(state: PagingState<Int, SafetyNotificationResponse.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SafetyNotificationResponse.Item> {
        val currentPage = params.key ?: 1

        return try {
            dataGoKrNetworkApi.getDrugSafeLetterList(
                pageNo = currentPage,
            ).onDataGokrResponse().fold(
                onSuccess = {
                    val nextKey = it.body.let { body ->
                        if (body.items.size < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }

                    LoadResult.Page(
                        data = it.body.items,
                        prevKey = null,
                        nextKey = nextKey,
                    )
                },
                onFailure = { LoadResult.Error(it) },
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
