package com.android.mediproject.core.domain

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.medicine.medicinedetailinfo.toDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        itemName: String,
    ): Flow<Result<MedicineDetatilInfoDto>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfo(
            itemName = itemName,
        ).map { result ->
            result.fold(onSuccess = {
                Result.success(it.toDto())
            }, onFailure = {
                Result.failure(it)
            })
        }.flowOn(
            defaultDispatcher
        ).collectLatest {
            send(it)
        }
    }
}