package com.android.mediproject.feature.comments.commentsofamedicine

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.mediproject.core.common.bindingadapter.ISendText
import com.android.mediproject.core.common.network.Dispatcher
import com.android.mediproject.core.common.network.MediDispatchers
import com.android.mediproject.core.domain.EditCommentUseCase
import com.android.mediproject.core.domain.GetCommentsUseCase
import com.android.mediproject.core.domain.GetAccountStateUseCase
import com.android.mediproject.core.model.comments.Comment
import com.android.mediproject.core.model.navargs.MedicineBasicInfoArgs
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.model.user.AccountState
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_DELETE_MY_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_EDIT_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_LIKE
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.CLICKED_REPLY
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_APPLY_COMMENT_REPLY
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_APPLY_EDITED_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_DELETE_COMMENT
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.COMPLETED_LIKE
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineCommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val editCommentUseCase: EditCommentUseCase,
    private val getAccountStateUseCase: GetAccountStateUseCase,
    @Dispatcher(MediDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ISendText {

    private val _action =
        MutableSharedFlow<CommentActionState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 3)
    val action get() = _action.asSharedFlow()

    private val _medicineBasicInfo =
        MutableSharedFlow<MedicineBasicInfoArgs>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 2)
    val medicineBasicInfo get() = _medicineBasicInfo.asSharedFlow()

    private val _myUserId = MutableStateFlow<Long>(-1)
    private val myUserId get() = _myUserId.asStateFlow()

    private val _accountState = MutableStateFlow<AccountState>(AccountState.SignedOut)
    val accountState get() = _accountState.asStateFlow()

    private val replyId = MutableStateFlow<Long>(-1)

    init {
        viewModelScope.launch {
            getAccountStateUseCase().collectLatest {
                _accountState.value = it
                if (it is AccountState.SignedIn) {
                    _myUserId.value = it.myId
                }
            }
        }
    }


    val comments: StateFlow<PagingData<Comment>> = medicineBasicInfo.flatMapLatest { info ->
        getCommentsUseCase.getCommentsByMedicineId(info.medicineIdInAws, myUserId.value).cachedIn(viewModelScope).mapLatest { pagingData ->
            val signedIn = accountState.value is AccountState.SignedIn
            pagingData.map { comment ->
                comment.apply {
                    onClickReply = ::onClickedReply
                    onClickLike = ::onClickedLike

                    // 로그인 상태 파악 후
                    // 내가 쓴 댓글이면 수정, 삭제 가능하도록 메서드 참조 설정
                    if (comment.userId == myUserId.value && signedIn) {
                        onClickEdit = ::onClickedEdit
                        onClickDelete = ::onClickedDelete
                        onClickApplyEdited = ::applyEditedComment
                        isMine = true
                    }
                }
            }
        }
    }.flowOn(ioDispatcher).stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())


    /**
     * 답글 등록
     *
     * @param comment 답글 내용
     */
    private fun applyReplyComment(comment: String) {
        viewModelScope.launch {
            editCommentUseCase.applyNewComment(
                NewCommentParameter(
                    medicineId = medicineBasicInfo.replayCache.last().medicineIdInAws.toString(),
                    userId = myUserId.value.toString(),
                    content = comment,
                    subOrdinationId = replyId.value.toString(),
                ),
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 등록 성공
                    getCommentsUseCase.scrollChannel.send(Unit)
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY(Result.success(Unit)))
                }.onFailure {
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY(Result.failure(it)))
                }
            }
        }
    }

    /**
     * 새 댓글 등록
     */
    private fun applyNewComment(content: String) {
        viewModelScope.launch {
            editCommentUseCase.applyNewComment(
                NewCommentParameter(
                    medicineId = medicineBasicInfo.replayCache.last().medicineIdInAws.toString(),
                    userId = myUserId.value.toString(),
                    content = content,
                    subOrdinationId = "0",
                ),
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 등록 성공
                    getCommentsUseCase.scrollChannel.send(Unit)
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY(Result.success(Unit)))
                }.onFailure {
                    _action.emit(COMPLETED_APPLY_COMMENT_REPLY(Result.failure(it)))
                }
            }
        }
    }

    /**
     * 수정한 댓글(답글) 등록
     *
     * @param comment 수정한 댓글(답글) 정보
     */
    private fun applyEditedComment(comment: Comment) {
        viewModelScope.launch {
            editCommentUseCase.applyEditedComment(
                EditCommentParameter(
                    commentId = comment.commentId,
                    content = comment.content,
                    medicineId = medicineBasicInfo.replayCache.last().medicineIdInAws,
                ),
            ).collectLatest { result ->
                result.onSuccess {
                    // 댓글 수정 성공
                    _action.emit(COMPLETED_APPLY_EDITED_COMMENT(Result.success(Unit)))
                }.onFailure {
                    _action.emit(COMPLETED_APPLY_EDITED_COMMENT(Result.failure(it)))
                }
            }
        }
    }


    /**
     * 답글 작성하기 버튼 클릭
     * - 답글 등록하기 버튼 클릭이 아님
     *
     * @param comment 답글을 작성할 댓글의 id
     * @param commentId 답글을 작성할 댓글의 id
     */
    private fun onClickedReply(comment: String, commentId: Long) {
        viewModelScope.launch {
            replyId.emit(commentId)
            _action.emit(CLICKED_REPLY(comment))
        }
    }


    /**
     * 댓글 삭제 클릭
     *
     * @param commentId 삭제할 댓글의 id
     */
    private fun onClickedDelete(commentId: Long) {
        viewModelScope.launch {
            _action.tryEmit(CLICKED_DELETE_MY_COMMENT(commentId))
        }
    }

    /**
     * 댓글 삭제 처리
     */
    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            editCommentUseCase.deleteComment(DeleteCommentParameter(commentId, medicineBasicInfo.replayCache.last().medicineIdInAws))
                .collectLatest { result ->
                    result.onSuccess {
                        // 댓글 삭제 성공
                        getCommentsUseCase.scrollChannel.send(Unit)
                        _action.emit(COMPLETED_DELETE_COMMENT(Result.success(Unit)))
                    }.onFailure {
                        _action.emit(COMPLETED_DELETE_COMMENT(Result.failure(it)))
                    }
                }
        }
    }

    /**
     * 댓글 좋아요 클릭
     * - 좋아요 등록 또는 해제를 처리함
     *
     * @param commentId LIKE를 등록또는 해제 할 댓글의 id
     */
    private fun onClickedLike(commentId: Long, isLiked: Boolean) {
        viewModelScope.launch {
            editCommentUseCase.likeComment(LikeCommentParameter(commentId, medicineBasicInfo.replayCache.last().medicineIdInAws, isLiked))
                .collectLatest { result ->
                    result.onSuccess {
                        // like 처리 완료
                        _action.emit(COMPLETED_LIKE(Result.success(Unit)))
                    }.onFailure {
                        _action.emit(COMPLETED_LIKE(Result.failure(it)))
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
    private fun onClickedEdit(item: Comment, position: Int) {
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
            if (text.isEmpty()) _action.tryEmit(COMPLETED_APPLY_COMMENT_REPLY(Result.failure(IllegalArgumentException("댓글 내용이 없습니다."))))
            else {
                if (replyId.value == -1L) {
                    applyNewComment(text.toString())
                } else {
                    applyReplyComment(text.toString())
                }
            }
        }
    }

    fun setMedicineBasicInfo(medicineBasicInfo: MedicineBasicInfoArgs) {
        viewModelScope.launch {
            _medicineBasicInfo.emit(medicineBasicInfo)
        }
    }

    fun cancelReply() {
        viewModelScope.launch {
            _action.emit(CommentActionState.CANCELED_REPLY)
            replyId.value = -1
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
 * @property NONE 초기 상태
 */
@Suppress("ClassName")
sealed class CommentActionState {

    /**
     * @property commentId 삭제할 댓글의 id
     */
    data class CLICKED_DELETE_MY_COMMENT(val commentId: Long) : CommentActionState()

    /**
     * @property comment 답글 내용
     */
    data class CLICKED_REPLY(val comment: String) : CommentActionState()
    object CANCELED_REPLY : CommentActionState()
    object CLICKED_LIKE : CommentActionState()

    /**
     * @property position 수정할 댓글의 리스트 내 절대 위치
     */
    data class CLICKED_EDIT_COMMENT(val position: Int) : CommentActionState()

    data class COMPLETED_APPLY_COMMENT_REPLY(val result: Result<Unit>) : CommentActionState()
    data class COMPLETED_APPLY_EDITED_COMMENT(val result: Result<Unit>) : CommentActionState()
    data class COMPLETED_LIKE(val result: Result<Unit>) : CommentActionState()
    data class COMPLETED_DELETE_COMMENT(val result: Result<Unit>) : CommentActionState()

    object NONE : CommentActionState()
}
