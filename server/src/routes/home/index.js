"use strict";

const express = require("express");
const router = express.Router();

router.get("/", (req, res) => {
    res.send("hello!");
})

module.exports = router;