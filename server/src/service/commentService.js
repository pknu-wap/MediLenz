"use strict";

const { Comment, Like } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");

const addLike = async (userId, commentId) => {
    try {
        const newLike = await Like.create({
            USERID: userId,
            COMMENTID: commentId
        });
        return responseFormat(200, { message: responseMsg.LIKE_ADD_COMPLETE, likeId: newLike.ID });
    } catch (err) {
        return responseFormat(500, { message: responseMsg.LIKE_ADD_FAIL });
    }
}
const removeLike = async (userId, commentId) => {
    try {
        await Like.destroy({
            where: {
                USERID: userId,
                COMMENTID: commentId
            }
        });
        return responseFormat(200, { message: responseMsg.LIKE_REMOVE_COMPLETE });
    } catch (err) {
        return responseFormat(500, { message: responseMsg.LIKE_REMOVE_FAIL });
    }
}
const isLiked = async (userId, commentId) => {
    try {
        const like = await Like.findOne({
            where: {
                USERID: userId,
                COMMENTID: commentId
            }
        });
        if (!like) {
            return responseFormat(200, { message: responseMsg.LIKE_NOT_FOUND });
        }
        return responseFormat(200, { message: responseMsg.LIKE_FOUND });
    } catch (err) {
        return responseFormat(500, { message: responseMsg.LIKE_NOT_FOUND });
    }
}
const getLikeList = async (commentId) => {
    try {
        const likeList = await Like.findAll({
            where: {
                COMMENTID: commentId
            }
        });
        _likeList = [];
        for (let i in likeList) {
            _likeList.push(likeList[i].dataValues);
        }
        return _likeList;
    } catch (err) {
        return [];
    }
}
const writeComment = async (userId, medicineId, content, subOrdinationId) => {
    const newComment = await Comment.create({
        USERID: userId,
        MEDICINEID: medicineId,
        CONTENT: content,
        SUBORDINATION: subOrdinationId
    });
    return responseFormat(200, { message: responseMsg.COMMENT_WRITE_COMPLETE, commentId: newComment.ID });
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
        CONTENT: "삭제된 댓글입니다."
    }, {
        where: {
            ID: commentId
        }
    });
    return true;
}
const editComment = async (commentId, content) => {
    const targetComment = await Comment.findOne({
        where: {
            ID: commentId
        }
    });
    if (!targetComment) {
        return false;
    }
    await Comment.update({
        CONTENT: content
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
    let _commentList = [];
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        if (_comment.SUBORDINATION != 0) continue;
        _comment.likeList = await getLikeList(_comment.ID);
        _comment.replies = [];
        _commentList.push(_comment);
    }
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        if (_comment.SUBORDINATION == 0) continue;
        for (let j in _commentList) {
            if (_comment.SUBORDINATION == _commentList[j].ID) {
                _comment.likeList = await getLikeList(_comment.ID);
                _commentList[j].replies.push(_comment);
                break;
            }
        }
    }
    return _commentList;
}

module.exports = {
    addLike,
    removeLike,
    writeComment,
    deleteComment,
    editComment,
    getCommentList,
    isLiked
};