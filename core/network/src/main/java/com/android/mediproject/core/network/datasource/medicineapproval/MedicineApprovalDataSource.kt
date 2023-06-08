package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.model.medicine.medicineapproval.MedicineApprovalListResponse
import com.android.mediproject.core.model.medicine.medicinedetailinfo.MedicineDetailInfoResponse
import kotlinx.coroutines.flow.Flow

interface MedicineApprovalDataSource {

    /**
     * 의약품 허가 목록 조회
     *
     * @param itemName 의약품명
     * @param entpName 업체명
     * @param pageNo 페이지 번호
     */
    suspend fun getMedicineApprovalList(
        itemName: String?,
        entpName: String?,
        medicationType: String?,
        pageNo: Int,
    ): Result<MedicineApprovalListResponse>

    fun getMedicineDetailInfo(
        itemName: String,
    ): Flow<Result<MedicineDetailInfoResponse>>

    fun getMedicineDetailInfoByItemSeq(
        itemSeqs: List<String>
    ): Flow<Result<List<MedicineDetailInfoResponse>>>
}