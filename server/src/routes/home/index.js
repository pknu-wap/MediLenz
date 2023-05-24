"use strict";

const router = require("express").Router();

router.post("/", (req, res) => {
    res.send(req.body);
})

/**
 * @swagger
 * tags:
 *   name: Users
 *   description: 유저 관리 API
 */
const user = require("../user/userRouter");
router.use("/user", user);

module.exports = router;