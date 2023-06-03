"use strict";

const { responseFormat } = require("../../../config/response")
const responseMsg = require("../../../config/responseMsg");
const { addFavoriteMedicine } = require("../../../service/medicineService");

// GET
const output = {

}

// POST
const process = {
    // Add favorite medicine
    // [POST] /medicine/favorite
    addFavoriteMedicine: async (req, res) => {
        const { userId } = req.verifiedToken;
        const { medicineId } = req.body;
        const result = await addFavoriteMedicine(userId, medicineId); // add favorite medicine
        return res.status(result.code).send(result.response);
    }
}

// PUT
const edit = {

}

// DELETE
const eliminate = {

}

module.exports = {
    output,
    process,
    edit,
    eliminate,
};