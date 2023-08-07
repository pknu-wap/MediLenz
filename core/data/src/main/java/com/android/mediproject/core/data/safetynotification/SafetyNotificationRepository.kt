package com.android.mediproject.core.data.safetynotification

import androidx.paging.PagingData
import com.android.mediproject.core.model.medicine.safetynotification.SafetyNotificationResponse
import kotlinx.coroutines.flow.Flow

interface SafetyNotificationRepository {
    fun getSafetyNotificationList(
    ): Flow<PagingData<SafetyNotificationResponse.Item>>
}
