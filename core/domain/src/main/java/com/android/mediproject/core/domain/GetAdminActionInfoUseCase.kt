package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.remote.adminaction.AdminActionRepository
import com.android.mediproject.core.model.remote.adminaction.AdminActionListItemDto
import com.android.mediproject.core.model.remote.adminaction.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAdminActionInfoUseCase @Inject constructor(
    private val adminActionRepository: AdminActionRepository,
) {

    suspend fun getAdminActionList(
    ): Flow<PagingData<AdminActionListItemDto>> =
        adminActionRepository.getAdminActionList().let { pager ->
            pager.map { pagingData ->
                pagingData.map {
                    it.toDto()
                }
            }
        }

}