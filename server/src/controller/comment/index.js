"use strict";

const {getMedicineId} = require("../../service/medicineService");
const {writeComment, deleteComment, getCommentList, editComment, addLike, removeLike, isLiked} = require("../../service/commentService");

//userId is in req.verifiedToken
const get = async (req, res) => {
    //item_seq, item_name, item_ingr_name, prduct_type, entp_name, spclty_pblc as POST
    const {ITEM_SEQ, ITEM_NAME, ITEM_INGR_NAME, PRDUCT_TYPE, ENTP_NAME, SPCLTY_PBLC} = req.body;
    const result = await getMedicineId(ITEM_SEQ, ITEM_NAME, ITEM_INGR_NAME, PRDUCT_TYPE, ENTP_NAME, SPCLTY_PBLC);
    return res.status(result.code).send(result.response);
}
const getComment = async (req, res) => {
    const {medicineId} = req.query;
    const result = await getCommentList(medicineId);
    return res.status(result.code).send(result.response);
}
const writeTest = async (req, res) => {
    const {userId, medicineId, content, subOrdinationId} = req.body;
    const result = await writeComment(userId, medicineId, content, subOrdinationId);
    return res.status(result.code).send(result.response);
}
const patch = async (req, res) => {
    const {medicineId, commentId, content} = req.body;
    const {userId} = req.verifiedToken;
    const result = await editComment(userId, medicineId, commentId, content);
    return res.status(result.code).send(result.response);
}
const del = async (req, res) => {
    const {medicineId, commentId} = req.body;
    const {userId} = req.verifiedToken;
    const result = await deleteComment(userId, medicineId, commentId);
    return res.status(result.code).send(result.response);
}
const likeGet = async (req, res) => {
    const {commentId} = req.query; 
    const {userId} = req.verifiedToken;
    const result = await isLiked(userId, commentId);
    return res.status(result.code).send(result.response);
}
const likePost = async (req, res) => { //addLike
    const {commentId} = req.body;
    const {userId} = req.verifiedToken;
    const result = await addLike(userId, commentId);
    return res.status(result.code).send(result.response);
}
const likeDelete = async (req, res) => { //removeLike
    const {commentId} = req.body;
    const {userId} = req.verifiedToken;
    const result = await removeLike(userId, commentId);
    return res.status(result.code).send(result.response);
}

module.exports = {
    get,
    getComment,
    writeTest,
    patch,
    del,
    likeGet,
    likePost,
    likeDelete
}