package com.android.mediproject.core.data.remote.comments

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.AWS_LOAD_PAGE_SIZE
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsDataSource: CommentsDataSource
) : CommentsRepository {
    override suspend fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<MedicineCommentsResponse>> =
        Pager(config = PagingConfig(pageSize = AWS_LOAD_PAGE_SIZE, prefetchDistance = 5), pagingSourceFactory = {
            CommentsListDataSourceImpl(
                commentsDataSource, itemSeq
            )
        }).flow


}