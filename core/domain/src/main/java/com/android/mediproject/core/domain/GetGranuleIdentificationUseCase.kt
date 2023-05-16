package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepository
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoDto
import com.android.mediproject.core.model.remote.granule.toDto
import javax.inject.Inject

class GetGranuleIdentificationUseCase @Inject constructor(
    private val repository: GranuleIdentificationRepository
) {


    /**
     * 약품 식별 정보 조회
     *
     * @param itemName 약품명
     * @param entpName 업체명
     * @param itemSeq 약품 고유 번호
     */
    suspend operator fun invoke(
        itemName: String?, entpName: String?, itemSeq: String?
    ): Result<GranuleIdentificationInfoDto> =
        repository.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).map {
            it.toDto()
        }
}