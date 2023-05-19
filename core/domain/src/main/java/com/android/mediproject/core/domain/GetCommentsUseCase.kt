package com.android.mediproject.core.domain

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor() {


    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     */
    fun getCommentsForAMedicine(itemSeq: String): Flow<PagingData<CommentDto>> {
        TODO()
    }

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO()
    }

}