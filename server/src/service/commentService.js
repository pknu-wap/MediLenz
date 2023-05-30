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
        //delete comment with no subordination
        await Comment.destroy({
            where: {
                ID: commentId
            }
        });
        return true;
    }
    //replace content with "삭제된 댓글입니다."
    await Comment.update({
        COMMENT: "삭제된 댓글입니다."
    }, {
        where: {
            ID: commentId
        }
    });
    return true;
}