"use strict";

const { Comment } = require("../models/index");
const writeComment = async (userId, medicineId, comment, subOrdinationId) => {
    const newComment = await Comment.create({
        USERID: userId,
        MEDICINEID: medicineId,
        COMMENT: comment,
        SUBORDINATIONID: subOrdinationId
    });
    return newComment.ID;
}
const deleteComment = async (commentId) => {
    const comment = await Comment.findOne({
        where: {
            ID: commentId
        }
    });
    if (!comment) {
        return false;
    }
    if (comment.SUBORDINATIONID == 0) {
        await Comment.destroy({
            where: {
                ID: commentId
            }
        });
        return true;
    }
    await Comment.update({
        COMMENT: "삭제된 댓글입니다."
    }, {
        where: {
            ID: commentId
        }
    });
    return true;
}
const editComment = async (commentId, comment) => {
    const targetComment = await Comment.findOne({
        where: {
            ID: commentId
        }
    });
    if (!targetComment) {
        return false;
    }
    await Comment.update({
        COMMENT: comment
    }, {
        where: {
            ID: commentId
        }
    });
    return true;
}
const getCommentList = async (medicineId) => {
    const commentList = await Comment.findAll({
        where: {
            MEDICINEID: medicineId
        }
    });

    alignedCommentList = []
    for (let comment in commentList) {
        if (comment.SUBORDINATION == 0) continue;
        comment.replies = [];
        alignedCommentList.push(comment);
    }

    for (let i = 0; i < commentList.length; i++) {
        if (commentList[i].SUBORDINATION != 0) {
            for (let j = 0; j < alignedCommentList.length; j++) {
                if (commentList[i].SUBORDINATION == alignedCommentList[j].ID) {
                    alignedCommentList[j].replies.push(commentList[i]);
                }
            }
        }
    }
    return commentList;
}