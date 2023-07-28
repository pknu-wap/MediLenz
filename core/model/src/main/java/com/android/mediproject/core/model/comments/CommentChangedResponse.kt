package com.android.mediproject.core.model.comments

import com.android.mediproject.core.model.servercommon.ServerQueryResponse
import kotlinx.serialization.Serializable

/**
 * 댓글 등록,수정,삭제,좋아요 처리 후 서버로 부터 받는 응답
 */
@Serializable
data class CommentChangedResponse(
    val commentId: Long = 0L,
) : ServerQueryResponse()
