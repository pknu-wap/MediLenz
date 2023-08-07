package com.android.mediproject.core.domain

import com.android.mediproject.core.data.safetynotification.SafetyNotificationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSafetyNotificationUseCase @Inject constructor(
    private val safetyNotificationRepository: SafetyNotificationRepository,
) {
    operator fun invoke() = safetyNotificationRepository.getSafetyNotificationList()
}
