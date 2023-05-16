package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepository
import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionResponse
import javax.inject.Inject

class GetElderlyCautionUseCase @Inject constructor(
    private val repository: ElderlyCautionRepository
) {


    suspend operator fun invoke(
        itemName: String?, itemSeq: String?
    ): Result<ElderlyCautionResponse.Body.Item> =
        repository.getElderlyCaution(itemName = itemName, itemSeq = itemSeq)
}