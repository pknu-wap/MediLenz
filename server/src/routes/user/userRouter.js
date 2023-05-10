"use strict";

const userCrtl = require('../../controller/user/user.ctrl');

const router = require("express").Router();

router.post("/login", userCrtl.process.login); // sign-in
//router.head("/refresh", userCrtl.output)
router.post("/register", userCrtl.process.register); // sign-up

module.exports = router;