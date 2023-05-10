"use strict";

const userCrtl = require('../../controller/user/user.ctrl');
const { verifyRefreshToken } = require("../../config/jwt");
const router = require("express").Router();

router.post("/login", userCrtl.process.login); // sign-in
router.post("/reissue", verifyRefreshToken, userCrtl.process.reissue); // reissuing token
router.post("/register", userCrtl.process.register); // sign-up

module.exports = router;