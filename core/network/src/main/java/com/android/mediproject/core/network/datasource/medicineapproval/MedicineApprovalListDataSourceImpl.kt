package com.android.mediproject.core.network.datasource.medicineapproval

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.medicine.medicineapproval.Item

class MedicineApprovalListDataSourceImpl(
    private val medicineApprovalDataSource: MedicineApprovalDataSource,
    private val itemName: String?,
    private val entpName: String?,
    private val medicationType: String?,
) : PagingSource<Int, Item>() {

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val currentPage = params.key ?: 1

        return try {
            medicineApprovalDataSource.getMedicineApprovalList(
                itemName = itemName,
                entpName = entpName,
                medicationType = medicationType,
                pageNo = currentPage,
            ).fold(onSuccess = {
                val nextKey = it.body.let { body ->
                    if (body.items.count() < DATA_GO_KR_PAGE_SIZE) null
                    else currentPage + 1
                }

                LoadResult.Page(
                    data = it.body.items,
                    prevKey = null,
                    nextKey = nextKey,
                )
            }, onFailure = { LoadResult.Error(it) })

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}