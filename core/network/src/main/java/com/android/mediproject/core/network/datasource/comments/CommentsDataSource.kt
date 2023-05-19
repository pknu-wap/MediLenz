package com.android.mediproject.core.network.datasource.comments

import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse

interface CommentsDataSource {

    suspend fun getCommentsForAMedicine(itemSeq: String): Result<List<MedicineCommentsResponse>>
}