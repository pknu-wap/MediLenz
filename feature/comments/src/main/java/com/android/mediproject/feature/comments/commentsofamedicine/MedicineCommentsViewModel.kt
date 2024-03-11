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
import com.android.mediproject.core.model.comments.BaseComment
import com.android.mediproject.core.model.comments.Comment
import com.android.mediproject.core.model.navargs.MedicineBasicInfoArgs
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.ui.base.BaseViewModel
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.None
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnClickEditComment
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnClickToDeleteComment
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnClickToLike
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnClickToReply
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnCompleteApplyCommentOrReply
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnCompleteApplyEditComment
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnCompleteDeleteComment
import com.android.mediproject.feature.comments.commentsofamedicine.CommentActionState.OnCompleteLike
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.pknujsp.core.annotation.KBindFunc
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MedicineCommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val editCommentUseCase: EditCommentUseCase,
    private val accountSessionRepository: AccountSessionRepository,
    @Dispatcher(MediDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel(), ISendText {

    private val _action = MutableSharedFlow<CommentActionState>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
    val action = _action.asSharedFlow()

    private val _medicineBasicInfo = MutableStateFlow<MedicineBasicInfoArgs?>(null)
    private val medicineBasicInfo = _medicineBasicInfo.asStateFlow()

    private val _myId = MutableStateFlow(NONE_USER_ID)
    private val myId = _myId.asStateFlow()

    private val _replyId = MutableStateFlow(NONE_REPLY_ID)
    private val replyId = _replyId.asStateFlow()

    private companion object {
        const val NONE_REPLY_ID = -1L
        const val NONE_USER_ID = -1L
    }

    init {
        viewModelScope.launch {
            getAccountStateUseCase().collectLatest {
                _accountState.value = it
                it.onSignedIn { myId, _, _ ->
                    _myId.value = myId
                }
            }
        }
    }

    val comments: StateFlow<PagingData<Comment>> = medicineBasicInfo.filterNotNull().flatMapLatest { info ->
        getCommentsUseCase.getCommentsByMedicineId(info.medicineIdInAws, myId.value).cachedIn(viewModelScope).mapLatest { pagingData ->
            val signedIn = accountState.value is AccountState.SignedIn
            pagingData.map { comment ->
                comment.apply {
                    onClickReply = ::onClickedReply
                    onClickLike = ::onClickedLike

                    // 로그인 상태 파악 후
                    // 내가 쓴 댓글이면 수정, 삭제 가능하도록 메서드 참조 설정
                    if (comment.userId == myId.value && signedIn) {
                        onClickEdit = ::onClickedEdit
                        onClickDelete = ::onClickedDelete
                        onClickApplyEdited = ::editComment
                        isMine = true
                    }
                }
            }
        }
    }.flowOn(ioDispatcher).stateIn(viewModelScope, SharingStarted.Eagerly, PagingData.empty())


    private fun applyReply(comment: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                editCommentUseCase.applyNewComment(
                    NewCommentParameter(
                        medicineId = medicineBasicInfo.filterNotNull().last().medicineIdInAws.toString(),
                        userId = myId.value.toString(),
                        content = comment,
                        parentId = replyId.value.toString(),
                    ),
                )
            }.onSuccess {
                _action.emit(OnCompleteApplyCommentOrReply(true))
            }.onFailure {
                _action.emit(OnCompleteApplyCommentOrReply(false))
            }
        }
    }

    private fun applyNewComment(content: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                editCommentUseCase.applyNewComment(
                    NewCommentParameter(
                        medicineId = medicineBasicInfo.value!!.medicineIdInAws.toString(),
                        userId = myId.value.toString(),
                        content = content,
                    ),
                )
            }.onSuccess {
                _action.emit(OnCompleteApplyCommentOrReply(true))
            }.onFailure {
                _action.emit(OnCompleteApplyCommentOrReply(false))
            }
        }
    }

    private fun editComment(comment: Comment) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                editCommentUseCase.applyEditedComment(
                    EditCommentParameter(
                        commentId = comment.commentId,
                        medicineId = medicineBasicInfo.value!!.medicineIdInAws,
                        content = comment.content,
                    ),
                )
            }.onSuccess {
                _action.emit(OnCompleteApplyEditComment(true))
            }.onFailure {
                _action.emit(OnCompleteApplyEditComment(false))
            }
        }
    }


    /**
     * 답글 작성 시작하기 버튼 클릭
     * - 답글 등록하기 버튼 클릭이 아님
     *
     * @param comment 답글을 작성할 댓글의 정보
     */
    private fun onClickedReply(comment: BaseComment) {
        viewModelScope.launch {
            _replyId.value = comment.commentId
            _action.emit(OnClickToReply(comment))
        }
    }


    private fun onClickedDelete(commentId: Long) {

    }

    fun deleteComment(commentId: Long) {

    }

    private fun onClickedLike(commentId: Long, isLiked: Boolean) {

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
            _action.emit(OnClickEditComment(position))
        }
    }

    override fun onClickedSendButton(text: CharSequence) {
        viewModelScope.launch {
            if (text.isEmpty()) {
                _action.emit(OnCompleteApplyCommentOrReply(false))
            } else {
                if (replyId.value == NONE_REPLY_ID) applyNewComment(text.toString())
                else applyReply(text.toString())
            }
        }
    }

    fun setMedicineBasicInfo(medicineBasicInfo: MedicineBasicInfoArgs) {
        viewModelScope.launch {
            _medicineBasicInfo.emit(medicineBasicInfo)
        }
    }

}

/**
 * 댓글 액션 상태
 *
 * @property OnClickToDeleteComment 내가 쓴 댓글 삭제 클릭
 * @property OnClickToReply 답글 입력하기 클릭
 * @property OnClickToLike 댓글 좋아요 클릭
 * @property OnClickEditComment 댓글 수정 클릭
 * @property OnCompleteApplyCommentOrReply 댓글/답글 등록 완료
 * @property OnCompleteApplyEditComment 댓글 수정 완료
 * @property OnCompleteLike 댓글 좋아요 완료
 * @property OnCompleteDeleteComment 댓글 삭제 완료
 * @property None 초기 상태
 */
@KBindFunc
sealed interface CommentActionState {

    /**
     * @property commentId 삭제할 댓글의 id
     */
    data class OnClickToDeleteComment(val commentId: Long) : CommentActionState

    object OnClickToComment : CommentActionState


    data class OnClickToReply(val baseComment: BaseComment) : CommentActionState
    object OnClickToLike : CommentActionState

    /**
     * @property positionOnList 수정할 댓글의 리스트 내 절대 위치
     */
    data class OnClickEditComment(val positionOnList: Int) : CommentActionState

    data class OnCompleteApplyCommentOrReply(val success: Boolean) : CommentActionState
    data class OnCompleteApplyEditComment(val success: Boolean) : CommentActionState
    data class OnCompleteLike(val success: Boolean) : CommentActionState
    data class OnCompleteDeleteComment(val success: Boolean) : CommentActionState
    object None : CommentActionState
}
