package com.android.mediproject.feature.comments.commentsofamedicine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.bindingadapter.ISendText
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.CommentsUseCase
import com.android.mediproject.core.model.comments.CommentDto
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_DELETE_MY_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_EDIT_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_LIKE
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_REPLY
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_APPLY_COMMENT_REPLY
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_APPLY_EDITED_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_DELETE_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_LIKE
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.ERROR
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineCommentsViewModel @Inject constructor(
    private val commentsUseCase: CommentsUseCase,
    private val savedStateHandle: SavedStateHandle,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ISendText {

    private val myUserId = savedStateHandle.getStateFlow("myUserId", 3)

    private val _action = MutableSharedFlow<CommentActionState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1
    )

    private val medicindItemSeq = savedStateHandle.getStateFlow("medicineItemSeq", "")

    val action get() = _action.asSharedFlow()

    val comments: StateFlow<PagingData<CommentDto>> = medicindItemSeq.flatMapLatest { itemSeq ->
        commentsUseCase.getCommentsForAMedicine(itemSeq).flatMapLatest {
            val id = myUserId.value
            it.map { commentDto ->
                commentDto.apply {
                    onClickReply = ::onClickedReply
                    onClickLike = ::onClickedLike

                    // 내가 쓴 댓글이면 수정, 삭제 가능하도록 메서드 참조 설정
                    if (commentDto.userId == id) {
                        onClickEdit = ::onClickedEdit
                        onClickDelete = ::onClickedDelete
                        onClickApplyEdited = ::applyEditedComment
                        isMine = true
                    }
                }
            }
            flowOf(it)
        }
    }.flowOn(ioDispatcher).cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())


    /**
     * 답글 등록
     *
     * @param comment 답글 내용
     * @param subordinationId 부모 댓글의 id
     */
    private fun applyReplyComment(comment: String, subordinationId: Int) {
        viewModelScope.launch {
            commentsUseCase.applyNewComment(
                NewCommentDto(
                    medicineId = medicindItemSeq.value,
                    userId = myUserId.value,
                    content = comment,
                    subordinationId = subordinationId
                )
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 등록 성공
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY)
                }.onFailure {
                    _action.emit(ERROR(it.message ?: "Failed"))
                }
            }
        }
    }

    /**
     * 수정한 댓글(답글) 등록
     *
     * @param commentDto 수정한 댓글(답글) 정보
     */
    private fun applyEditedComment(commentDto: CommentDto) {
        viewModelScope.launch {
            commentsUseCase.applyEditedComment(
                EditedCommentDto(
                    commentId = commentDto.commentId, content = commentDto.content
                )
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 수정 성공
                    _action.emit(COMPLETED_APPLY_EDITED_COMMENT)
                }.onFailure {
                    _action.emit(ERROR(it.message ?: "Failed"))
                }
            }
        }
    }


    /**
     * 답글 작성하기 버튼 클릭
     * - 답글 등록하기 버튼 클릭이 아님
     *
     * @param position 답글 작성 아이템 뷰가 표시될 리스트내 절대 위치
     */
    private fun onClickedReply(position: Int) {
        viewModelScope.launch {
            _action.tryEmit(CLICKED_REPLY(position))
        }
    }


    /**
     * 댓글 삭제 클릭
     *
     * @param commentId 삭제할 댓글의 id
     */
    private fun onClickedDelete(commentId: Int) {
        viewModelScope.launch {
            _action.tryEmit(CLICKED_DELETE_MY_COMMENT(commentId))
        }
    }

    /**
     * 댓글 좋아요 클릭
     * - 좋아요 등록 또는 해제를 처리함
     *
     * @param commentId LIKE를 등록또는 해제 할 댓글의 id
     */
    private fun onClickedLike(commentId: Int) {
        viewModelScope.launch {
            commentsUseCase.likeComment(commentId).collectLatest { result ->
                result.onSuccess {
                    // like 처리 완료
                    _action.emit(COMPLETED_LIKE)
                }.onFailure {
                    _action.emit(ERROR(it.message ?: "Failed"))
                }
            }
        }
    }

    /**
     * 댓글 수정하기 클릭
     * - 수정할 내용을 입력 한 후 서버에 업데이트하는 로직이 아님
     *
     * @param item 수정할 댓글의 정보
     * @param position 수정할 댓글의 리스트 내 절대 위치
     */
    private fun onClickedEdit(item: CommentDto, position: Int) {
        viewModelScope.launch {
            // 수정 상태 변경
            item.isEditing = !item.isEditing
            _action.tryEmit(CLICKED_EDIT_COMMENT(position))
        }
    }


    /**
     * 댓글 등록하기 버튼 클릭
     *
     * @param text 댓글 내용
     *
     */
    override fun onClickedSendButton(text: CharSequence) {
        viewModelScope.launch {
            if (text.isEmpty()) _action.tryEmit(ERROR("댓글 내용을 입력해주세요."))
            else commentsUseCase.applyNewComment(
                NewCommentDto(
                    medicineId = medicindItemSeq.value,
                    userId = myUserId.value,
                    content = text.toString(),
                    subordinationId = -1
                )
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 등록 성공
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY)
                }.onFailure {
                    _action.emit(ERROR(it.message ?: "Failed"))
                }
            }
        }
    }
}

/**
 * 댓글 액션 상태
 *
 * @property CLICKED_DELETE_MY_COMMENT 내가 쓴 댓글 삭제 클릭
 * @property CLICKED_REPLY 답글 입력하기 클릭
 * @property CLICKED_LIKE 댓글 좋아요 클릭
 * @property CLICKED_EDIT_COMMENT 댓글 수정 클릭
 * @property COMPLETED_APPLY_COMMENT_REPLY 댓글/답글 등록 완료
 * @property COMPLETED_APPLY_EDITED_COMMENT 댓글 수정 완료
 * @property COMPLETED_LIKE 댓글 좋아요 완료
 * @property COMPLETED_DELETE_COMMENT 댓글 삭제 완료
 * @property ERROR 댓글 등록, 수정, 삭제, 좋아요 에러
 * @property NONE 초기 상태
 */
@Suppress("ClassName")
sealed class CommentActionState {

    /**
     * @property commentId 삭제할 댓글의 id
     */
    data class CLICKED_DELETE_MY_COMMENT(val commentId: Int) : CommentActionState()

    /**
     * @property position 답글 입력하기 클릭한 댓글의 리스트 내 절대 위치
     */
    data class CLICKED_REPLY(val position: Int) : CommentActionState()
    object CLICKED_LIKE : CommentActionState()

    /**
     * @property position 수정할 댓글의 리스트 내 절대 위치
     */
    data class CLICKED_EDIT_COMMENT(val position: Int) : CommentActionState()

    object COMPLETED_APPLY_COMMENT_REPLY : CommentActionState()
    object COMPLETED_APPLY_EDITED_COMMENT : CommentActionState()
    object COMPLETED_LIKE : CommentActionState()
    object COMPLETED_DELETE_COMMENT : CommentActionState()

    /**
     * @property errorMessage 댓글 등록, 수정, 삭제, 좋아요 에러 메시지
     */
    data class ERROR(val errorMessage: String) : CommentActionState()
    object NONE : CommentActionState()
}