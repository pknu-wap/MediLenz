"use strict";

const router = require("express").Router();
const commentController = require("../../../controller/comment");
//verifyAccessToken
const { verifyAccessToken } = require("../../../config/jwt");

// Comment
router.post("/", commentController.get);
router.post("/writeTest", verifyAccessToken, commentController.writeTest);

// Like
router.get("/like", verifyAccessToken, commentController.likeGet);
router.post("/like", verifyAccessToken, commentController.likePost);
router.delete("/like", verifyAccessToken, commentController.likeDelete);
module.exports = router;