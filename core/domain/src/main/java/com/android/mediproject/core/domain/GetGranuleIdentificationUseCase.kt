package com.android.mediproject.core.domain

import com.android.mediproject.core.data.granule.GranuleIdentificationRepository
import com.android.mediproject.core.model.granule.GranuleIdentificationInfo
import com.android.mediproject.core.model.granule.toGranuleIdentificationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGranuleIdentificationUseCase @Inject constructor(
    private val repository: GranuleIdentificationRepository,
) {


    /**
     * 약품 식별 정보 조회
     *
     * @param itemName 약품명
     * @param entpName 업체명
     * @param itemSeq 약품 고유 번호
     */
    operator fun invoke(
        itemName: String?, entpName: String?, itemSeq: String?,
    ): Flow<Result<GranuleIdentificationInfo>> =
        repository.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq).map { result ->
            result.fold(
                onSuccess = { response ->
                    Result.success(response.toGranuleIdentificationInfo())
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        }
}
