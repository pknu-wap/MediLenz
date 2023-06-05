package com.android.mediproject.core.model.comments


import com.android.mediproject.core.model.remote.base.BaseAwsQueryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentListResponse(
    @SerialName("commentList") val commentList: List<Comment>) : BaseAwsQueryResponse() {

    @Serializable
    data class Comment(
        @SerialName("CONTENT") val content: String, // 테스트 - 메인 댓글 1
        @SerialName("createdAt") val createdAt: String, // 2023-06-02T10:43:12.435Z
        @SerialName("ID") val id: Long, // 2
        @SerialName("likeList") val likeList: List<Int>,
        @SerialName("MEDICINEID") val medicineId: Long, // 41
        @SerialName("replies") val replies: List<Reply>,
        @SerialName("SUBORDINATION") val subordination: Long, // 0
        @SerialName("USERID") val userId: Long, // 2
        @SerialName("updatedAt") val updatedAt: String) {

        @Serializable
        data class Reply(
            @SerialName("CONTENT") val content: String, // 테스트 - 서브 댓글 1 - 1
            @SerialName("createdAt") val createdAt: String, // 2023-06-02T10:43:54.211Z
            @SerialName("ID") val iD: Long, // 5
            @SerialName("likeList") val likeList: List<Int>,
            @SerialName("MEDICINEID") val medicineId: Long, // 41
            @SerialName("SUBORDINATION") val subordination: Long, // 2
            @SerialName("USERID") val userId: Long, // 3
            @SerialName("updatedAt") val updatedAt: String)
    }
}