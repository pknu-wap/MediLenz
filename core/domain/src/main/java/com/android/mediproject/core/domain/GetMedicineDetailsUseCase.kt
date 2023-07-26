package com.android.mediproject.core.domain

import com.android.mediproject.core.data.medicineapproval.MedicineApprovalRepository
import com.android.mediproject.core.data.medicineid.MedicineIdRepository
import com.android.mediproject.core.database.cache.manager.MedicineDataCacheManager
import com.android.mediproject.core.model.navargs.MedicineInfoArgs
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetail
import com.android.mediproject.core.model.medicine.medicinedetailinfo.cache.MedicineCacheEntity
import com.android.mediproject.core.model.medicine.medicinedetailinfo.toMedicineDetail
import com.android.mediproject.core.model.requestparameters.GetMedicineIdParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetMedicineDetailsUseCase @Inject constructor(
    private val medicineApprovalRepository: MedicineApprovalRepository,
    private val medicineIdRepository: MedicineIdRepository,
    private val medicineDataCacheManager: MedicineDataCacheManager,
) {

    operator fun invoke(
        medicineInfoArgs: MedicineInfoArgs,
    ): Flow<Result<MedicineDetail>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfo(
            itemName = medicineInfoArgs.itemKorName,
        ).zip(
            medicineIdRepository.getMedicineId(
                GetMedicineIdParameter(
                    entpName = medicineInfoArgs.entpKorName,
                    itemIngrName = medicineInfoArgs.itemIngrName,
                    itemName = medicineInfoArgs.itemKorName,
                    itemSeq = medicineInfoArgs.itemSeq.toString(),
                    productType = medicineInfoArgs.productType,
                    medicineType = medicineInfoArgs.medicineType,
                ),
            ),
        ) { r1, r2 ->
            r1 to r2
        }.collectLatest { result ->
            val medicineInfoResult = result.first
            val medicineIdResult = result.second

            val medicineId = medicineIdResult.fold(
                onSuccess = { item ->
                    item.medicineId
                },
                onFailure = {
                    0
                },
            )

            val medicineInfo = medicineInfoResult.fold(
                onSuccess = { item ->
                    Result.success(item.toMedicineDetail(medicineId))
                },
                onFailure = {
                    Result.failure(it)
                },
            )

            trySend(medicineInfo)
        }
    }

    fun updateImageCache(itemSeq: String, imageUrl: String) {
        medicineDataCacheManager.updateImage(
            MedicineCacheEntity(
                itemSequence = itemSeq,
                imageUrl = imageUrl,
            ),
        )
    }


    fun getMedicineDetailInfoByItemSeq(itemSeqs: List<String>): Flow<Result<List<MedicineDetail>>> = channelFlow {
        medicineApprovalRepository.getMedicineDetailInfoByItemSeq(itemSeqs).collectLatest { medicineInfoResult ->
            val medicineInfo = medicineInfoResult.fold(
                onSuccess = { item ->
                    Result.success(
                        item.map {
                            it.toMedicineDetail()
                        },
                    )
                },
                onFailure = {
                    Result.failure(it)
                },
            )

            trySend(medicineInfo)
        }
    }
}
