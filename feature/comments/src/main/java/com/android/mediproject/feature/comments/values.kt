package com.android.mediproject.feature.comments

enum class CommentActionType {
    EDIT, DELETE, REPLY, LIKE, CANCEL_EDIT, APPLYED_EDITED_COMMENT, APPLYED_NEW_COMMENT;
}

data class CommentAction(val position: Int, val commentActionType: CommentActionType)