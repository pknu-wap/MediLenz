package com.android.mediproject.core.domain

import androidx.paging.PagingData
import androidx.paging.flatMap
import com.android.mediproject.core.data.comments.CommentsRepository
import com.android.mediproject.core.model.comments.Comment
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.comments.toComment
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository,
) {

    val scrollChannel = Channel<Unit>(capacity = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    /**
     * 약에 대한 댓글을 가져오는 메서드입니다.
     *
     * @param medicineId 약의 고유 번호
     * @param myId 내 ID
     */
    fun getCommentsByMedicineId(medicineId: Long, myId: Long): Flow<PagingData<Comment>> = channelFlow {
        commentsRepository.getCommentsByMedicineId(medicineId).collectLatest { pagingData ->
            val result = pagingData.flatMap {
                (it.replies.map { reply ->
                    reply.toComment().apply {
                        reply.likeList.forEach { like ->
                            if (like.userId == myId) this.isLiked = true
                        }
                    }
                }.toList().reversed()) + listOf(
                    it.toComment().apply {
                        it.likeList.forEach { like ->
                            if (like.userId == myId) this.isLiked = true
                        }
                    },
                )
            }
            send(result)
        }
    }

    /**
     * 내가 작성한 댓글을 가져오는 메서드입니다.
     */
    fun getMyCommentsList(): Flow<Result<MyCommentsListResponse>> = commentsRepository.getMyCommentsList().mapLatest { result ->
        result.fold(onSuccess = { Result.success(it) }, onFailure = { Result.failure(it) })
    }
}
