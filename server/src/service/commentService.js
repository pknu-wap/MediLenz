"use strict";

const { Comment, Like } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");
const {_getUserNickname} = require("./userService");
const _getLikeList = async (commentId) => { //NOT FOR EXPORT
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
const _getCommentList = async (medicineId) => { //NOT FOR EXPORT
    const commentList = await Comment.findAll({
        where: {
            MEDICINEID: medicineId
        }
    });
    let _commentList = [];
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        if (_comment.SUBORDINATION != 0) continue;
        _comment.likeList = await _getLikeList(_comment.ID);
        _comment.nickname = await _getUserNickname(_comment.USERID);
        _comment.replies = [];
        _commentList.push(_comment);
    }
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        if (_comment.SUBORDINATION == 0) continue;
        for (let j in _commentList) {
            if (_comment.SUBORDINATION == _commentList[j].ID) {
                _comment.nickname = await _getUserNickname(_comment.USERID);
                _comment.likeList = await _getLikeList(_comment.ID);
                _commentList[j].replies.push(_comment);
                break;
            }
        }
    }
    return _commentList;
}
const _getReplyList = async (medicineId, commentId) => { //NOT FOR EXPORT
    const commentList = await Comment.findAll({
        where: {
            MEDICINEID: medicineId,
            SUBORDINATION: commentId
        }
    });
    let _commentList = [];
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        _commentList.push(_comment);
    }
    return _commentList;
}
const getCommentList = async (medicineId) => {
    const commentList = await _getCommentList(medicineId);
    return responseFormat(200, { message: responseMsg.COMMENT_GET_COMPLETE, commentList: commentList });
}
const getCommentListFromUserId = async (userId) => {
    const commentList = await Comment.findAll({
        where: {
            USERID: userId
        }
    });
    let _commentList = [];
    for (let i in commentList) {
        let _comment = commentList[i].dataValues;
        _commentList.push(_comment);
    }
    return responseFormat(200, { message: responseMsg.COMMENT_GET_COMPLETE, commentList: _commentList });
}
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
const writeComment = async (userId, medicineId, content, subOrdinationId) => {
    try {
        const newComment = await Comment.create({
            USERID: userId,
            MEDICINEID: medicineId,
            CONTENT: content,
            SUBORDINATION: subOrdinationId
        });
        return responseFormat(200, { message: responseMsg.COMMENT_WRITE_COMPLETE, commentId: newComment.ID });
    } catch (err) {
        return responseFormat(500, { message: responseMsg.COMMENT_WRITE_FAIL });
    }
}
const deleteComment = async (medicineId, commentId) => {
    try {
        const transaction = await sequelize.transaction();
        const comment = await Comment.findOne({
            where: {
                ID: commentId,
                MEDICINEID: medicineId
            }
        }, { transaction });
        if (!comment) {
            await transaction.rollback();
            return responseFormat(404, { message: responseMsg.COMMENT_NOT_FOUND });
        }
        if (_getReplyList(commentId, medicineId).length == 0) { //댓글에 달린 답글이 없을 경우
            const likeList = await _getLikeList(commentId);
            if (likeList.length != 0) { //댓글에 달린 좋아요가 있을 경우
                await Like.destroy({
                    where: {
                        COMMENTID: commentId
                    }
                }, { transaction });
            } 
            await Comment.destroy({
                where: {
                    ID: commentId
                }
            }, { transaction });
            await transaction.commit();
            return responseFormat(200, { message: responseMsg.COMMENT_DELETE_COMPLETE });
        }
        // 댓글에 달린 답글이 있을 경우
        await Comment.update({
            CONTENT: "삭제된 댓글입니다."
        }, {
            where: {
                ID: commentId
            }
        }, { transaction });
        await transaction.commit();
        return responseFormat(200, { message: responseMsg.COMMENT_DELETE_COMPLETE });
    } catch (err) {
        await transaction.rollback();
        return responseFormat(500, { message: responseMsg.COMMENT_DELETE_FAIL });
    }
}
const editComment = async (userId, medicineId, commentId, content) => {
    try {
        const targetComment = await Comment.findOne({
            where: {
                ID: commentId,
                MEDICINEID: medicineId,
            }
        });
        if (!targetComment) {
            return responseFormat(404, { message: responseMsg.COMMENT_NOT_FOUND });
        }
        if (targetComment.USERID != userId) {
            console.log('userId: ', userId);
            console.log('targetComment.USERID: ', targetComment.USERID);
            return responseFormat(403, { message: responseMsg.COMMENT_EDIT_FAIL });
        }
        await Comment.update({
            CONTENT: content
        }, {
            where: {
                ID: commentId,
                MEDICINEID: medicineId
            }
        });
        return responseFormat(200, { message: responseMsg.COMMENT_EDIT_COMPLETE });
    }
    catch (err) {
        return responseFormat(500, { message: responseMsg.COMMENT_EDIT_FAIL });
    }
}


module.exports = {
    addLike,
    removeLike,
    writeComment,
    deleteComment,
    editComment,
    isLiked,
    getCommentList,
    getCommentListFromUserId
};