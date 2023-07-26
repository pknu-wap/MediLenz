package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.adminaction.AdminActionRepository
import com.android.mediproject.core.model.adminaction.AdminAction
import com.android.mediproject.core.model.adminaction.toAdminAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAdminActionInfoUseCase @Inject constructor(
    private val adminActionRepository: AdminActionRepository,
) {

    suspend fun getAdminActionList(
    ): Flow<PagingData<AdminAction>> =
        adminActionRepository.getAdminActionList().let { pager ->
            pager.map { pagingData ->
                pagingData.map {
                    it.toAdminAction()
                }
            }
        }

}
