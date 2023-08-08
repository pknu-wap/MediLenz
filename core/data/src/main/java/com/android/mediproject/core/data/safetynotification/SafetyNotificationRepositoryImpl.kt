package com.android.mediproject.core.data.safetynotification

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.news.safetynotification.SafetyNotificationResponse
import com.android.mediproject.core.network.datasource.safetynotification.SafetyNotificationDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SafetyNotificationRepositoryImpl @Inject constructor(
    private val safetyNotificationDataSource: SafetyNotificationDataSource,
) : SafetyNotificationRepository {
    override fun getSafetyNotificationList(): Flow<PagingData<SafetyNotificationResponse.Item>> = Pager(
        config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE, prefetchDistance = 5),
        pagingSourceFactory = {
            safetyNotificationDataSource
        },
    ).flow
}
