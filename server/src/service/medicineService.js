"use strict";

const { Medicine } = require("../models/index");
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
        return responseFormat(200, {message: responseMsg.MEDICINE_ID_FOUND, medicineId: newMedicine.ID});
    }
    return responseFormat(200, {message: responseMsg.MEDICINE_ID_FOUND, medicineId: medicine.ID});
}

module.exports = {
    getMedicineId
}