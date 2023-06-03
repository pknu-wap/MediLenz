"use strict";

const router = require("express").Router();
const favoriteCtrl = require("../../../controller/medicine/favorite/favorite.ctrl");
const { verifyAccessToken } = require("../../../config/jwt");

// Favorite
router.get("/", verifyAccessToken, favoriteCtrl.output.getFavoriteMedicineList); // add favorite medicine
router.post("/", verifyAccessToken, favoriteCtrl.process.addFavoriteMedicine); // add favorite medicine

module.exports = router;