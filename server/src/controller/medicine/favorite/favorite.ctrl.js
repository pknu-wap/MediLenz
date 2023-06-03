"use strict";

const { addFavoriteMedicine, getFavoriteMedicineList, deleteFavoriteMedicine } = require("../../../service/medicineService");

// GET
const output = {
    // GET favorite medicine list
    // [GET] /medicine/favorite
    getFavoriteMedicineList: async (req, res) => {
        const { userId } = req.verifiedToken;
        const result = await getFavoriteMedicineList(userId); // get favorite medicine list
        return res.status(result.code).send(result.response);
    }
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
    // Delete favorite medicine
    // [DELETE] /medicine/favorite/:medicineId
    deleteFavoriteMedicine: async (req, res) => {
        const { userId } = req.verifiedToken;
        const { medicineId } = req.query;
        const result = await deleteFavoriteMedicine(userId, medicineId); // delete favorite medicine
        return res.status(result.code).send(result.response);
    }
}

module.exports = {
    output,
    process,
    edit,
    eliminate,
};