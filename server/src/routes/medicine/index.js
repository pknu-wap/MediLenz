"use strict";

const router = require("express").Router();
const commentRouter = require('./comment');
const favoriteRouter = require('./favorite');

router.use("/comment", commentRouter);
router.use("/favorite", favoriteRouter); // favorite medicine
module.exports = router;