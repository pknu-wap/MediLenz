package com.android.mediproject.core.data.remote.medicineapproval

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.DATA_GO_KR_PAGE_SIZE
import com.android.mediproject.core.model.remote.medicineapproval.Item
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalDataSource
import com.android.mediproject.core.network.datasource.medicineapproval.MedicineApprovalListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MedicineApprovalRepositoryImpl @Inject constructor(private val medicineApprovalDataSource: MedicineApprovalDataSource) :
    MedicineApprovalRepository {

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
    override fun getMedicineApprovalList(itemName: String?, entpName: String?): Flow<PagingData<Item>> = Pager(
        config = PagingConfig(pageSize = DATA_GO_KR_PAGE_SIZE),
        pagingSourceFactory = {
            MedicineApprovalListDataSourceImpl(
                medicineApprovalDataSource, itemName, entpName
            )
        }
    ).flow
}