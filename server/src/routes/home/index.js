"use strict";

const router = require("express").Router();

router.get("/", (req, res) => {
    res.send("hello!");
})

const user = require("../user/userRouter");
router.use("/user", user);

module.exports = router;