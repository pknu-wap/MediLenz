package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.granule.GranuleIdentificationRepository
import com.android.mediproject.core.model.remote.granule.GranuleIdentificationInfoResponse
import javax.inject.Inject

class GetGranuleIdentificationUseCase @Inject constructor(
    private val repository: GranuleIdentificationRepository
) {


    suspend operator fun invoke(
        itemName: String?, entpName: String?, itemSeq: String?
    ): Result<GranuleIdentificationInfoResponse.Body.Item> =
        repository.getGranuleIdentificationInfo(itemName = itemName, entpName = entpName, itemSeq = itemSeq)
}