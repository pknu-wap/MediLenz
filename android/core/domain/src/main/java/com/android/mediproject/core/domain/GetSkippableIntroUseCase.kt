package com.android.mediproject.core.domain

import com.android.mediproject.core.datastore.AppDataStore
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetSkippableIntroUseCase @Inject constructor(
    private val appDataStore: AppDataStore
) {
    suspend operator fun invoke() = callbackFlow {
        appDataStore.skipIntro.collectLatest {
            trySend(it)
        }
        close()
    }

    suspend fun saveSkipIntro(skipIntro: Boolean) = appDataStore.saveSkipIntro(skipIntro)
}