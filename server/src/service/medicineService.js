"use strict";

const { Medicine, FavoriteMedicine } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");

const getMedicineId = async (item_seq, item_name, item_ingr_name, prduct_type, entp_name, spclty_pblc) => {
    const medicine = await Medicine.findOne({
        where: {
            ITEM_SEQ: item_seq,
            ITEM_NAME: item_name,
            ITEM_INGR_NAME: item_ingr_name,
            PRDUCT_TYPE: prduct_type,
            ENTP_NAME: entp_name,
            SPCLTY_PBLC: spclty_pblc
        }
    });
    if (!medicine) {
        //Add medicine data to db
        const newMedicine = await Medicine.create({
            ITEM_SEQ: item_seq,
            ITEM_NAME: item_name,
            ITEM_INGR_NAME: item_ingr_name,
            PRDUCT_TYPE: prduct_type,
            ENTP_NAME: entp_name,
            SPCLTY_PBLC: spclty_pblc
        });
        return responseFormat(200, { message: responseMsg.MEDICINE_ID_FOUND, medicineId: newMedicine.ID });
    }
    return responseFormat(200, { message: responseMsg.MEDICINE_ID_FOUND, medicineId: medicine.ID });
}

// Search medicine through medicine ID
const getMedicineData = async (ID) => {
    const medicine = await Medicine.findOne({
        where: {
            ID
        }
    });
    return medicine.dataValues;
}

// Get favorite medicine list 
const getFavoriteMedicineList = async (user_id) => {
    try {
        const favorite_medicine_id_list = await FavoriteMedicine.findAll({ // search favorite medicine id list
            where: {
                USERID: user_id
            }
        });
        const promises = await favorite_medicine_id_list.map((data) => { // Get medicine data through ID -> promise
            return getMedicineData(data.MEDICINEID);
        });
        const medicine_list = await Promise.all(promises); // resolve promises
        return responseFormat(200, { message: responseMsg.MEDICINE_FAVORITE_LIST_GET_COMPLETE, medicineList: medicine_list }); // search SUCCESS
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.MEDICINE_FAVORITE_LIST_GET_FAIL }); // search FAIL
    }
}

// Add favorite medicine
const addFavoriteMedicine = async (user_id, medicine_id) => {
    try {
        const newFavoriteMedicine = await FavoriteMedicine.create({ // insert new favorite medicine into DB
            USERID: user_id,
            MEDICINEID: medicine_id
        });
        return responseFormat(201, { message: responseMsg.MEDICINE_FAVORITE_ADD_COMPLETE, medicineList: newFavoriteMedicine.ID }); // insert SUCCESS
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.MEDICINE_FAVORITE_ADD_FAIL }); // insert FAIL
    }
}

module.exports = {
    getMedicineId,
    getFavoriteMedicineList,
    addFavoriteMedicine
}