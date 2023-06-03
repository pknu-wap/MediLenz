"use strict";

const router = require("express").Router();
const favoriteCtrl = require("../../../controller/medicine/favorite/favorite.ctrl");
const { verifyAccessToken } = require("../../../config/jwt");

// Favorite
router.post("/", verifyAccessToken, favoriteCtrl.process.addFavoriteMedicine); // add favorite medicine

module.exports = router;