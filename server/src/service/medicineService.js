"use strict";

const { Medicine } = require("../models/index");

const getMedicineId = async (item_seq, item_name, item_ingr_name, prduct_type, entp_name, spclty_pblc, ingredient_name) => {
    const medicine = await Medicine.findOne({
        attributes: ["ID"],
        where: {
            ITEM_SEQ: item_seq,
            ITEM_NAME: item_name,
            ITEM_INGR_NAME: item_ingr_name,
            PRDUCT_TYPE: prduct_type,
            ENTP_NAME: entp_name,
            SPCLTY_PBLC: spclty_pblc,
            INGREDIENT_NAME: ingredient_name
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
            SPCLTY_PBLC: spclty_pblc,
            INGREDIENT_NAME: ingredient_name
        });
        return newMedicine.ID;
    }
    return medicine.ID;
}

module.exports = {
    getMedicineId
}