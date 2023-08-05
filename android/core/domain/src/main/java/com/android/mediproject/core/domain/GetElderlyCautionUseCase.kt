package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.elderlycaution.ElderlyCautionRepository
import com.android.mediproject.core.model.remote.elderlycaution.ElderlyCautionDto
import com.android.mediproject.core.model.remote.elderlycaution.toDto
import javax.inject.Inject

class GetElderlyCautionUseCase @Inject constructor(
    private val repository: ElderlyCautionRepository
) {


    suspend operator fun invoke(
        itemName: String?, itemSeq: String?
    ): Result<ElderlyCautionDto> = repository.getElderlyCaution(itemName = itemName, itemSeq = itemSeq)
        .fold(onSuccess = { Result.success(it.toDto()) }, onFailure = { Result.failure(it) })
}