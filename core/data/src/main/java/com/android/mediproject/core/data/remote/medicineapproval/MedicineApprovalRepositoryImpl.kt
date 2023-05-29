package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.search.SearchHistoryRepository
import com.android.mediproject.core.database.searchhistory.SearchHistoryDto
import com.android.mediproject.core.model.medicine.medicineapproval.Item
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalListDataSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MedicineApprovalRepositoryImpl @Inject constructor(
    private val medicineApprovalDataSource: MedicineApprovalDataSource,
    private val searchHistoryRepository: SearchHistoryRepository,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : MedicineApprovalRepository {

    /**
     * PagingData를 사용하여 페이징 처리를 하기 위해 Pager를 사용
     *
     * PagingConfig를 통해 한 페이지에 몇 개의 아이템을 보여줄지 설정
     *
     * 의약품 허가 목록을 가져옵니다.
     *
     * @param itemName 의약품명
     * @param entpName 업체명
     *
     */
    override suspend fun getMedicineApprovalList(itemName: String?, entpName: String?, medicationType: String?): Flow<PagingData<Item>> =
        if (itemName == null && entpName == null) {
            emptyFlow()
        } else {
            searchHistoryRepository.insertSearchHistory(SearchHistoryDto(itemName ?: entpName!!))
            Pager(config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE, prefetchDistance = 5), pagingSourceFactory = {
                MedicineApprovalListDataSourceImpl(
                    medicineApprovalDataSource, itemName, entpName, medicationType
                )
            }).flow
        }


    override suspend fun getMedicineDetailInfo(itemName: String): Flow<Result<MedicineDetailInfoResponse.Body.Item>> = channelFlow {
        medicineApprovalDataSource.getMedicineDetailInfo(itemName).map { result ->
            result.fold(onSuccess = { Result.success(it.body.items.first()) }, onFailure = { Result.failure(it) })
        }.collectLatest {
            send(it)
        }
    }
}