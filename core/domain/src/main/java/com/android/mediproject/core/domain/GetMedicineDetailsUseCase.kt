package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineid.MedicineIdRepository
import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.medicine.medicinedetailinfo.toDto
import com.android.mediproject.core.model.remote.token.TokenState
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
    private val medicineIdRepository: MedicineIdRepository,
    private val signRepository: SignRepository) {

    operator fun invoke(
        medicineInfoArgs: MedicineInfoArgs): Flow<Result<MedicineDetatilInfoDto>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfo(
            itemName = medicineInfoArgs.itemKorName,
        ).collectLatest { result ->
            result.fold(onSuccess = { item ->
                // 서버에서 의약품 ID를 가져온다.
                // 현재 액세스 토큰이 있는 경우에만 가능
                val token = signRepository.getCurrentTokens().last()
                if (token is TokenState.Valid) {
                    medicineIdRepository.getMedicineId(GetMedicineIdParameter(entpName = medicineInfoArgs.entpKorName,
                        itemIngrName = medicineInfoArgs.itemIngrName,
                        itemName = medicineInfoArgs.itemKorName,
                        itemSeq = medicineInfoArgs.itemSeq.toString(),
                        productType = medicineInfoArgs.productType,
                        medicineType = medicineInfoArgs.medicineType)).collectLatest { idResult ->
                        idResult.fold(onSuccess = {
                            trySend(Result.success(item.toDto(it.medicineId)))
                        }, onFailure = {
                            trySend(Result.success(item.toDto(0)))
                        })
                    }
                }
            }, onFailure = {
                trySend(Result.failure(it))
            })
        }
    }
}