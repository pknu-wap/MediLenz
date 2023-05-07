package com.android.mediproject.core.network.datasource.medicineapproval

import com.android.mediproject.core.model.remote.medicineapproval.MedicineApprovalListResponse

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
    ): MedicineApprovalListResponse
}