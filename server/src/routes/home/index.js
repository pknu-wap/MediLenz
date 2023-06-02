"use strict";

const router = require("express").Router();

router.get("/", (req, res) => {
    res.send("hello!");
})

/**
 * @swagger
 * tags:
 *   name: Users
 *   description: 유저 관리 API
 */
const user = require("../user/userRouter");
router.use("/user", user);

const medicine = require("../medicine");
router.use("/medicine", medicine);
module.exports = router;