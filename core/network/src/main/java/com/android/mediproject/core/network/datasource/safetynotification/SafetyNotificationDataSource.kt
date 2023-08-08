package com.android.mediproject.core.network.datasource.safetynotification

import androidx.paging.PagingSource
import com.android.mediproject.core.model.news.safetynotification.SafetyNotificationResponse

abstract class SafetyNotificationDataSource : PagingSource<Int, SafetyNotificationResponse.Item>()
