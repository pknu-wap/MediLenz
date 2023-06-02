package com.android.mediproject.core.network.datasource.comments

import androidx.paging.PagingData
import com.android.mediproject.core.model.comments.EditedCommentDto
import com.android.mediproject.core.model.comments.MyCommentDto
import com.android.mediproject.core.model.comments.NewCommentDto
import com.android.mediproject.core.model.remote.comments.MedicineCommentsResponse
import com.android.mediproject.core.network.module.AwsNetworkApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

class CommentsDataSourceImpl @Inject constructor(
    private val awsNetworkApi: AwsNetworkApi
) : CommentsDataSource {

    override suspend fun getCommentsForAMedicineCatching(itemSeq: String): Result<List<MedicineCommentsResponse>> {
        val userNames = listOf("김철수", "박영희", "이민수", "김영미", "최준호", "정혜진", "유지현", "박지훈", "김민서", "이지아")
        val comments = listOf("오늘 머리가 너무 아팠어요. 그래서 타이레놀을 먹었는데 금방 나아졌어요.",
            "타이레놀을 복용하니까 두통이 확실히 가셨어요.",
            "아이가 열이 나서 걱정했는데, 타이레놀을 줘보니 열도 잘 내려가고 좋네요.",
            "타이레놀 복용하면 빠르게 나아지지만, 긴 기간 복용하면 안 좋을 수 있으니 조심하세요.",
            "약을 먹을 때는 물과 함께 복용하는 것이 좋다고 들었습니다. 타이레놀도 마찬가지에요.",
            "우리 동네 약국에서는 타이레놀을 쉽게 구할 수 있어서 편리해요.",
            "복용 후에는 충분한 물을 마셔야 해요. 그래야 약이 잘 흡수되니까요.",
            "비타민과 함께 복용하면 건강에 더 도움이 되더라고요.",
            "비상약으로 집안에 항상 구비해두는 약 중 하나가 타이레놀이에요.",
            "약 복용 전에는 반드시 설명서를 꼼꼼히 읽는게 중요해요.",
            "약을 먹고 나면 잠시 쉬는 것도 중요해요. 특히 타이레놀을 복용한 후에는요.",
            "빈속에 약을 먹으면 위에 좋지 않을 수 있어요. 가급적이면 식사 후에 복용하세요.",
            "타이레놀은 먹고 나서 운전하는 것은 피하는게 좋아요.",
            "타이레놀 복용 후에는 알코올을 피해야 해요.",
            "제가 느낀 바로는, 타이레놀은 가벼운 두통에 특히 효과적인 것 같아요.")

        val rand = Random(System.currentTimeMillis())

        val commentList = List(comments.size) { index ->
            val userId = rand.nextInt(0, userNames.size)
            val time =
                "2023-${"%02d".format(rand.nextInt(5) + 1)}" + "-${"%02d".format(rand.nextInt(28) + 1)}" + "T${"%02d".format(rand.nextInt(24))}:${
                    "%02d".format(rand.nextInt(60))
                }:00"

            val isReply = rand.nextInt(0, userNames.size) > userNames.size - 3

            MedicineCommentsResponse(commentId = index,
                userId = userId,
                userName = userNames[userId],
                isReply = isReply,
                subordinationId = if (isReply) rand.nextInt(0, userNames.size) else -1,
                content = comments[index],
                createdAt = time,
                updatedAt = time)
        }

        return Result.success(commentList)
    }

    override fun getMyComments(userId: Int): Flow<PagingData<MyCommentDto>> {
        TODO("Not yet implemented")
    }

    override fun applyEditedComment(editedCommentDto: EditedCommentDto): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override fun applyNewComment(newCommentDto: NewCommentDto): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override fun deleteComment(commentId: Int): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override fun likeComment(commentId: Int): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }
}