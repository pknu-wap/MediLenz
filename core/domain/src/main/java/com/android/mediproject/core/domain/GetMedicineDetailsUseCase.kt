package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.remote.medicineid.MedicineIdRepository
import com.android.mediproject.core.data.remote.sign.SignRepository
import com.android.mediproject.core.model.local.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetatilInfoDto
import com.android.mediproject.core.model.medicine.medicinedetailinfo.toDto
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
    private val medicineIdRepository: MedicineIdRepository,
    private val signRepository: SignRepository) {

    operator fun invoke(
        medicineInfoArgs: MedicineInfoArgs): Flow<Result<MedicineDetatilInfoDto>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfo(
            itemName = medicineInfoArgs.itemKorName,
        ).zip(medicineIdRepository.getMedicineId(GetMedicineIdParameter(entpName = medicineInfoArgs.entpKorName,
            itemIngrName = medicineInfoArgs.itemIngrName,
            itemName = medicineInfoArgs.itemKorName,
            itemSeq = medicineInfoArgs.itemSeq.toString(),
            productType = medicineInfoArgs.productType,
            medicineType = medicineInfoArgs.medicineType))) { r1, r2 ->
            r1 to r2
        }.catch {
            trySend(Result.failure(it))
        }.collectLatest { result ->
            val medicineInfoResult = result.first
            val medicineIdResult = result.second

            val medicineId = medicineIdResult.fold(onSuccess = { item ->
                item.medicineId
            }, onFailure = {
                0
            })

            val medicineInfo = medicineInfoResult.fold(onSuccess = { item ->
                Result.success(item.toDto(medicineId))
            }, onFailure = {
                Result.failure(it)
            })

            trySend(medicineInfo)
        }
    }
}