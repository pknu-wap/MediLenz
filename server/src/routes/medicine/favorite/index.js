"use strict";

const router = require("express").Router();
const favoriteCtrl = require("../../../controller/medicine/favorite/favorite.ctrl");
const { verifyAccessToken } = require("../../../config/jwt");

// Favorite
router.get("/", verifyAccessToken, favoriteCtrl.output.getFavoriteMedicineList); // get favorite medicine (list)
router.post("/", verifyAccessToken, favoriteCtrl.process.addFavoriteMedicine); // add favorite medicine
router.delete("/", verifyAccessToken, favoriteCtrl.eliminate.deleteFavoriteMedicine); // delete favorite medicine

module.exports = router;