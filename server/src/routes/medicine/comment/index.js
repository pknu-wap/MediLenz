"use strict";

const router = require("express").Router();
const commentController = require("../../../controller/comment");

router.post("/", commentController.get);
router.post("/writeTest", commentController.writeTest);
module.exports = router;