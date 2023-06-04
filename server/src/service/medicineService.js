"use strict";

const { Medicine, FavoriteMedicine } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");

const getMedicineId = async (
    ITEM_SEQ,
    item_name = null,
    item_ingr_name = null,
    prduct_type = null,
    entp_name = null,
    spclty_pblc = null
) => {
    // Search medicine ID through ITEM_SEQ
    if (
        !(
            item_name &&
            item_ingr_name &&
            prduct_type &&
            entp_name &&
            spclty_pblc
        )
    ) {
        // Search medicine ID in DB
        const medicine = await Medicine.findOne({
            where: {
                ITEM_SEQ,
            },
            attributes: ["ID"],
        });
        return medicine.ID; // return medicine ID
    }
    // Search medicine ID through ALL PARAMS
    else {
        const medicine = await Medicine.findOne({
            where: {
                ITEM_SEQ,
                ITEM_NAME: item_name,
                ITEM_INGR_NAME: item_ingr_name,
                PRDUCT_TYPE: prduct_type,
                ENTP_NAME: entp_name,
                SPCLTY_PBLC: spclty_pblc,
            },
        });
        if (!medicine) {
            //Add medicine data to db
            const newMedicine = await Medicine.create({
                ITEM_SEQ,
                ITEM_NAME: item_name,
                ITEM_INGR_NAME: item_ingr_name,
                PRDUCT_TYPE: prduct_type,
                ENTP_NAME: entp_name,
                SPCLTY_PBLC: spclty_pblc,
            });
            return responseFormat(200, {
                message: responseMsg.MEDICINE_ID_FOUND,
                medicineId: newMedicine.ID,
            });
        }
        return responseFormat(200, {
            message: responseMsg.MEDICINE_ID_FOUND,
            medicineId: medicine.ID,
        });
    }
};

// check favorite medicine
const checkFavoriteMedicine = async (user_id, ITEM_SEQ) => {
    try {
        // get favorite medicine id
        const medicine_id = await getMedicineId(ITEM_SEQ);

        // get favorite medicine
        const favorite_medicine = await FavoriteMedicine.findOne({
            where: {
                USERID: user_id,
                ID: medicine_id,
            },
        });

        let isFavorite = false;
        // If favorite medicine exists
        if (favorite_medicine) {
            isFavorite = true; // isFavorite is true
        }
        return responseFormat(200, {
            message: responseMsg.MEDICINE_FAVORITE_GET_COMPLETE,
            isFavorite,
        });
    } catch (err) {
        console.log(err);
        return responseFormat(500, {
            message: responseMsg.MEDICINE_FAVORITE_GET_FAIL,
        }); // search FAIL
    }
};

// Search medicine through medicine ID
const getMedicineData = async (ID) => {
    const medicine = await Medicine.findOne({
        where: {
            ID,
        },
    });
    return medicine.dataValues;
};

// Get favorite medicine list
const getFavoriteMedicineList = async (user_id) => {
    try {
        // search favorite medicine id list
        const favorite_medicine_id_list = await FavoriteMedicine.findAll({
            where: {
                USERID: user_id,
            },
        });
        // Get medicine data through ID -> promise
        const promises = await favorite_medicine_id_list.map((data) => {
            return getMedicineData(data.MEDICINEID);
        });
        const medicine_list = await Promise.all(promises); // resolve promises
        return responseFormat(200, {
            message: responseMsg.MEDICINE_FAVORITE_LIST_GET_COMPLETE,
            medicineList: medicine_list,
        }); // search SUCCESS
    } catch (err) {
        console.log(err);
        return responseFormat(500, {
            message: responseMsg.MEDICINE_FAVORITE_LIST_GET_FAIL,
        }); // search FAIL
    }
};

// Add favorite medicine
const addFavoriteMedicine = async (user_id, medicine_id) => {
    try {
        // insert new favorite medicine into DB
        const newFavoriteMedicine = await FavoriteMedicine.create({
            USERID: user_id,
            MEDICINEID: medicine_id,
        });
        return responseFormat(201, {
            message: responseMsg.MEDICINE_FAVORITE_ADD_COMPLETE,
            favoriteMedicineID: newFavoriteMedicine.ID,
        }); // insert SUCCESS
    } catch (err) {
        console.log(err);
        return responseFormat(500, {
            message: responseMsg.MEDICINE_FAVORITE_ADD_FAIL,
        }); // insert FAIL
    }
};

// Delete favorite medicine
const deleteFavoriteMedicine = async (user_id, medicine_id) => {
    try {
        // delete favorite medicine
        await FavoriteMedicine.destroy({
            where: {
                USERID: user_id,
                MEDICINEID: medicine_id,
            },
        });
        return responseFormat(200, {
            message: responseMsg.MEDICINE_FAVORITE_DELETE_COMPLETE,
        }); // delete SUCCESS
    } catch (err) {
        console.log(err);
        return responseFormat(500, {
            message: responseMsg.MEDICINE_FAVORITE_DELETE_FAIL,
        }); // delete FAIL
    }
};

module.exports = {
    getMedicineId,
    getFavoriteMedicineList,
    addFavoriteMedicine,
    deleteFavoriteMedicine,
    checkFavoriteMedicine,
};
