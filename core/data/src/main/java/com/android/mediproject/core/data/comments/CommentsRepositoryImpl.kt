package com.android.mediproject.core.data.comments

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.mediproject.core.common.SERVER_PAGE_SIZE
import com.android.mediproject.core.data.token.TokenRepository
import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.comments.MyCommentsListResponse
import com.android.mediproject.core.model.token.TokenState
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.network.datasource.comments.CommentsDataSource
import com.android.mediproject.core.network.datasource.comments.CommentsListDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val commentsDataSource: CommentsDataSource, private val tokenRepository: TokenRepository,
) : CommentsRepository {
    override fun getCommentsByMedicineId(medicineId: Long): Flow<PagingData<CommentListResponse.Comment>> =
        Pager(
            config = PagingConfig(pageSize = SERVER_PAGE_SIZE, prefetchDistance = 0),
            pagingSourceFactory = {
                CommentsListDataSourceImpl(commentsDataSource, medicineId)
            },
        ).flow

    override fun getMyCommentsList(): Flow<Result<MyCommentsListResponse>> = channelFlow {
        checkToken().collectLatest { tokenState ->
            tokenState.onSuccess {
                commentsDataSource.getMyCommentsList().collectLatest {
                    trySend(it)
                }
            }.onFailure {
                trySend(Result.failure(it))
            }
        }
    }

    override fun editComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>> = channelFlow {
        checkToken().collectLatest { tokenState ->
            tokenState.onSuccess {
                commentsDataSource.editComment(parameter).collectLatest {
                    trySend(it)
                }
            }.onFailure {
                trySend(Result.failure(it))
            }
        }
    }


    override fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>> = channelFlow {
        checkToken().collectLatest { tokenState ->
            tokenState.onSuccess {
                commentsDataSource.applyNewComment(parameter).collectLatest {
                    trySend(it)
                }
            }.onFailure {
                trySend(Result.failure(it))
            }
        }
    }

    override fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>> = channelFlow {
        checkToken().collectLatest { tokenState ->
            tokenState.onSuccess {
                commentsDataSource.deleteComment(parameter).collectLatest {
                    trySend(it)
                }
            }.onFailure {
                trySend(Result.failure(it))
            }
        }
    }

    override fun likeComment(parameter: LikeCommentParameter): Flow<Result<LikeResponse>> = channelFlow {
        checkToken().collectLatest { tokenState ->
            tokenState.onSuccess {
                commentsDataSource.likeComment(parameter).collectLatest {
                    trySend(it)
                }
            }.onFailure {
                trySend(Result.failure(it))
            }
        }
    }

    private suspend fun checkToken() = tokenRepository.getCurrentTokens().last().let { tokenState ->
        when (tokenState) {
            is TokenState.Tokens.Valid -> {
                flowOf(Result.success(Unit))
            }

            is TokenState.Error -> {
                flowOf(Result.failure(tokenState.exception))
            }

            else -> {
                flowOf(Result.failure(Exception("로그인 해주세요")))
            }
        }
    }

}
