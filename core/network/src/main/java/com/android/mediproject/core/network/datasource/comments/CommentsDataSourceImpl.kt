package com.android.mediproject.core.network.datasource.comments

import com.android.mediproject.core.model.comments.CommentChangedResponse
import com.android.mediproject.core.model.comments.CommentListResponse
import com.android.mediproject.core.model.comments.LikeResponse
import com.android.mediproject.core.model.requestparameters.DeleteCommentParameter
import com.android.mediproject.core.model.requestparameters.EditCommentParameter
import com.android.mediproject.core.model.requestparameters.LikeCommentParameter
import com.android.mediproject.core.model.requestparameters.NewCommentParameter
import com.android.mediproject.core.network.module.AwsNetworkApi
import com.android.mediproject.core.network.onResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentsDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi,
) : CommentsDataSource {

    /**
     * 약품에 대한 댓글 리스트를 가져온다.
     * @param medicineId: 약품 고유 번호
     */
    override suspend fun getCommentsByMedicineId(medicineId: Long): Result<CommentListResponse> {
        return awsNetworkApi.getCommentsByMedicineId(medicineId).onResponse().fold(
            onSuccess = { response ->
                if (response.commentList.isEmpty()) Result.failure(Exception("댓글이 없습니다."))
                else Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        )
    }

    override suspend fun getMyCommentsList(): Flow<Result<CommentListResponse>> = flow {
        awsNetworkApi.getMyCommentsList().onResponse().fold(
            onSuccess = { response ->
                if (response.commentList.isEmpty()) Result.failure(Exception("내가 작성한 댓글이 없습니다."))
                else Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also { emit(it) }
    }

    override fun editComment(parameter: EditCommentParameter): Flow<Result<CommentChangedResponse>> = flow {
        awsNetworkApi.editComment(parameter).onResponse().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also { emit(it) }
    }

    override fun applyNewComment(parameter: NewCommentParameter): Flow<Result<CommentChangedResponse>> = flow {
        awsNetworkApi.applyNewComment(parameter).onResponse().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also { emit(it) }
    }

    override fun deleteComment(parameter: DeleteCommentParameter): Flow<Result<CommentChangedResponse>> = flow {
        awsNetworkApi.deleteComment(parameter).onResponse().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also { emit(it) }
    }

    override fun likeComment(likeCommentParameter: LikeCommentParameter): Flow<Result<LikeResponse>> = flow {
        likeCommentParameter.let {
            if (it.toLike) awsNetworkApi.likeComment(likeCommentParameter.medicineId, likeCommentParameter.commentId)
            else awsNetworkApi.unlikeComment(likeCommentParameter.medicineId, likeCommentParameter.commentId)
        }.onResponse().fold(
            onSuccess = { response ->
                Result.success(response)
            },
            onFailure = {
                Result.failure(it)
            },
        ).also { emit(it) }
    }

}
