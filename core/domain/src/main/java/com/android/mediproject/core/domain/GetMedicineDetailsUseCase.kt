package com.android.mediproject.core.domain

import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineid.MedicineIdRepository
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.medicine.medicinedetailinfo.toDto
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
    private val medicineIdRepository: MedicineIdRepository,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(
        itemName: String,
    ): Flow<Result<MedicineDetatilInfoDto>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfo(
            itemName = itemName,
        ).map { result ->
            result.fold(onSuccess = {
                // 서버에서 의약품 ID를 가져온다.

                val resultDto = it.toDto()
                val medicineIdInAws = medicineIdRepository.getMedicineId(GetMedicineIdParameter(entpName = "",
                    itemIngrName = resultDto.mainItemIngredient,
                    itemName = resultDto.itemName,
                    itemSeq = resultDto.itemSequence,
                    productType = resultDto.etcOtcCode,
                    medicineType = ""
                ))

                Result.success(it.toDto())
            }, onFailure = {
                Result.failure(it)
            })
        }.flowOn(defaultDispatcher).collectLatest {
            send(it)
        }
    }
}