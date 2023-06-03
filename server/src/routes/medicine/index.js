"use strict";

const router = require("express").Router();
const commentRouter = require('./comment');

router.use("/comment", commentRouter);
module.exports = router;