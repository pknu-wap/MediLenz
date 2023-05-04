"use strict";

const userCrtl = require('../../controller/user/user.ctrl');

const router = require("express").Router();

router.post("/register", userCrtl.process.register)

module.exports = router;