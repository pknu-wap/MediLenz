package com.android.mediproject.core.model.comments


import com.android.mediproject.core.model.servercommon.ServerQueryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentListResponse(
    @SerialName("commentList") val commentList: List<Comment>,
) : ServerQueryResponse() {

    @Serializable
    data class Comment(
        @SerialName("CONTENT") val content: String, // 테스트 - 메인 댓글 1
        @SerialName("createdAt") val createdAt: String, // 2023-06-02T10:43:12.435Z
        @SerialName("ID") val id: Long, // 2
        @SerialName("MEDICINEID") val medicineId: Long, // 41
        @SerialName("replies") val replies: List<Reply>,
        @SerialName("SUBORDINATION") val subordination: Long, // 0
        @SerialName("USERID") val userId: Long, // 2
        @SerialName("likeList") val likeList: List<Like>,
        @SerialName("nickname") val nickName: String,
        @SerialName("updatedAt") val updatedAt: String,
    ) {
        @Serializable
        data class Reply(
            @SerialName("CONTENT") val content: String, // 테스트 - 서브 댓글 1 - 1
            @SerialName("createdAt") val createdAt: String, // 2023-06-02T10:43:54.211Z
            @SerialName("ID") val id: Long, // 5
            @SerialName("likeList") val likeList: List<Like>, @SerialName("MEDICINEID") val medicineId: Long, // 41
            @SerialName("SUBORDINATION") val subordination: Long, // 2
            @SerialName("USERID") val userId: Long, // 3
            @SerialName("nickname") val nickName: String, @SerialName("updatedAt") val updatedAt: String,
        )
    }
}

@Serializable
data class Like(
    @SerialName("COMMENTID") val commentId: Long, // 2
    @SerialName("ID") val id: Long, // 1
    @SerialName("USERID") val userId: Long,
)
