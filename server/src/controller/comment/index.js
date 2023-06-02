"use strict";

const {getMedicineId} = require("../../service/medicineService");
const {writeComment, deleteComment, editComment, getCommentList} = require("../../service/commentService");

const get = async (req, res) => {
    //item_seq, item_name, item_ingr_name, prduct_type, entp_name, spclty_pblc as POST
    const {ITEM_SEQ, ITEM_NAME, ITEM_INGR_NAME, PRDUCT_TYPE, ENTP_NAME, SPCLTY_PBLC} = req.body;
    const result = await getMedicineId(ITEM_SEQ, ITEM_NAME, ITEM_INGR_NAME, PRDUCT_TYPE, ENTP_NAME, SPCLTY_PBLC);
    if (result.code != 200) { return res.status(result.code).send(result.response); }
    const medicineId = result.response.medicineId;
    const commentList = await getCommentList(medicineId);
    return res.status(200).send(commentList);
}
const writeTest = async (req, res) => {
    //userId, medicineId, comment, subOrdinationId as POST
    const {userId, medicineId, content, subOrdinationId} = req.body;
    console.log(userId, medicineId, content, subOrdinationId);
    const result = await writeComment(userId, medicineId, content, subOrdinationId);
    return res.status(result.code).send(result.response);
}
module.exports = {
    get,
    writeTest
}