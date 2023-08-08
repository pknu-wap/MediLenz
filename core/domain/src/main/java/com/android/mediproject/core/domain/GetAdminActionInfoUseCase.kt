package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.android.mediproject.core.data.adminaction.AdminActionRepository
import com.android.mediproject.core.model.common.UiModelMapperFactory
import com.android.mediproject.core.model.news.adminaction.AdminAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAdminActionInfoUseCase @Inject constructor(
    private val adminActionRepository: AdminActionRepository,
) {

    fun getAdminActionList(
    ): Flow<PagingData<AdminAction>> =
        adminActionRepository.getAdminActionList().let { pager ->
            pager.map { pagingData ->
                pagingData.map {
                    val wrapper = UiModelMapperFactory.create<AdminAction>(it)
                    wrapper.convert()
                }
            }
        }

}
