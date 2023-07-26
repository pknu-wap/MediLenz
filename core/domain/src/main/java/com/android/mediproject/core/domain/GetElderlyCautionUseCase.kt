package com.android.mediproject.core.domain

import com.android.mediproject.core.data.remote.senior.ElderlyCautionRepository
import com.android.mediproject.core.model.datagokr.durproduct.senior.ElderlyCautionDto
import com.android.mediproject.core.model.datagokr.durproduct.senior.toElderlyCautionDto
import javax.inject.Inject

class GetElderlyCautionUseCase @Inject constructor(
    private val repository: ElderlyCautionRepository,
) {


    suspend operator fun invoke(
        itemName: String?, itemSeq: String?,
    ): Result<ElderlyCautionDto> = repository.getElderlyCaution(itemName = itemName, itemSeq = itemSeq)
        .fold(onSuccess = { Result.success(it.toElderlyCautionDto()) }, onFailure = { Result.failure(it) })
}
