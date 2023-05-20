package com.android.mediproject.feature.comments.commentsofamedicine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.comments.CommentAction
import com.android.mediproject.feature.comments.CommentActionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineCommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val savedStateHandle: SavedStateHandle,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val myUserId = savedStateHandle.getStateFlow("myUserId", -1)

    private val _action = MutableSharedFlow<CommentAction>(
        replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    private val medicindItemSeq = savedStateHandle.getStateFlow("medicineItemSeq", "")

    val action get() = _action.asSharedFlow()

    val comments: Flow<PagingData<CommentDto>> = medicindItemSeq.flatMapLatest {
        getCommentsUseCase.getCommentsForAMedicine(it).map {
            val id = myUserId.value
            it.map { commentDto ->
                commentDto.apply {
                    onClickReply = ::onClickReply
                    onClickLike = ::onClickLike

                    // 내가 쓴 댓글이면 수정, 삭제 가능
                    if (commentDto.userId == id) {
                        onClickEditCancel = ::onClickEditCancel
                        onClickEdit = ::onClickEdit
                        onClickDelete = ::onClickDelete
                        onClickApplyEdited = ::applyEditedComment
                        isMine = true
                    }
                }
            }
        }.flowOn(ioDispatcher).cachedIn(viewModelScope)
    }

    /**
     * 새로운 댓글(답글) 등록
     */
    fun applyNewComment(comment: String) {
        viewModelScope.launch {
            NewCommentDto(medicineId = "placerat", userId = myUserId.value, content = "sds", subordinationId = -1).apply {

            }
        }
    }

    /**
     * 수정한 댓글(답글) 등록
     */
    private fun applyEditedComment(commentDto: CommentDto, position: Int) {
        viewModelScope.launch {
            EditedCommentDto(
                commentId = commentDto.commentId, content = commentDto.content
            ).apply {

            }
        }
    }


    /**
     * 답글 작성 클릭
     */
    private fun onClickReply(commentId: Int) {

    }

    /**
     * 댓글 삭제 클릭
     */
    private fun onClickDelete(commentId: Int) {
        viewModelScope.launch {
            _action.emit(CommentAction(commentId, CommentActionType.DELETE))
        }
    }

    /**
     * 댓글 좋아요 클릭
     */
    private fun onClickLike(commentId: Int) {

    }

    /**
     * 댓글 수정하기 클릭
     */
    private fun onClickEdit(position: Int) {
        viewModelScope.launch {
            _action.emit(CommentAction(position, CommentActionType.EDIT))
        }
    }

    /**
     * 댓글 수정 취소 클릭
     */
    private fun onClickEditCancel(position: Int) {
        viewModelScope.launch {
            _action.emit(CommentAction(position, CommentActionType.CANCEL_EDIT))
        }
    }
}