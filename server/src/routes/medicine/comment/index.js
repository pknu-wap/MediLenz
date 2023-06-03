"use strict";

const router = require("express").Router();
const commentController = require("../../../controller/comment");
const { verifyAccessToken } = require("../../../config/jwt");

// Comment
router.post("/writeTest", verifyAccessToken, commentController.writeTest); //CREATE
router.get("/", commentController.get); //READ
router.get("/:commentId", commentController.getComment); //READ
router.patch("/", verifyAccessToken, commentController.patch); //UPDATE
router.delete("/", verifyAccessToken, commentController.del); //DELETE

// Like
router.get("/like", verifyAccessToken, commentController.likeGet); //READ
router.post("/like", verifyAccessToken, commentController.likePost); //CREATE
router.delete("/like", verifyAccessToken, commentController.likeDelete); //DELETE
module.exports = router;