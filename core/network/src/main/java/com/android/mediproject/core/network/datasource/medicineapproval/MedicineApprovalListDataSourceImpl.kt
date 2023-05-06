package com.android.mediproject.core.network.datasource.medicineapproval

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.medicineapproval.Item

class MedicineApprovalListDataSourceImpl(
    private val medicineApprovalDataSource: MedicineApprovalDataSource,
    private val itemName: String?,
    private val entpName: String?,
) : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val currentPage = params.key ?: 1

        val response = medicineApprovalDataSource.getMedicineApprovalList(
            itemName = itemName,
            entpName = entpName,
            pageNo = currentPage,
        )

        return try {
            response.header.let {
                if (it.resultCode == "00") {
                    val nextKey = response.body?.let { body ->
                        if (body.items.count() < DATA_GO_KR_PAGE_SIZE) null
                        else currentPage + 1
                    }

                    LoadResult.Page(
                        data = response.body?.items ?: emptyList(),
                        prevKey = null,
                        nextKey = nextKey,
                    )
                } else {
                    LoadResult.Error(Throwable(it.resultMsg))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}