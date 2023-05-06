package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.model.remote.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.network.module.MedicineApprovalNetworkApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MedicineApprovalDataSourceImpl @Inject constructor(
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val medicineApprovalNetworkApi: MedicineApprovalNetworkApi
) : MedicineApprovalDataSource {


    override suspend fun getMedicineApprovalList(
        itemName: String?, entpName: String?, pageNo: Int
    ): MedicineApprovalListResponse = medicineApprovalNetworkApi.getApprovalList(itemName = itemName, entpName = entpName, pageNo = pageNo)

}