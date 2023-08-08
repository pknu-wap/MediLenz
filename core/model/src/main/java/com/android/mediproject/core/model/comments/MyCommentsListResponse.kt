package com.android.mediproject.core.model.comments


import com.android.mediproject.core.model.servercommon.ServerQueryResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyCommentsListResponse(
    @SerialName("commentList")
    val commentList: List<Comment>,
) : ServerQueryResponse() {
    @Serializable
    data class Comment(
        @SerialName("CONTENT")
        val content: String,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("ID")
        val id: Int,
        @SerialName("MEDICINEID")
        val medicineId: Int,
        @SerialName("SUBORDINATION")
        val subordination: Int,
        @SerialName("USERID")
        val userId: Int,
        @SerialName("updatedAt")
        val updatedAt: String
    )
}
