"use strict";

const router = require("express").Router();
const commentController = require("../../../controller/comment");
const { verifyAccessToken } = require("../../../config/jwt");

// Comment
router.post("/writeTest", verifyAccessToken, commentController.writeTest); //CREATE
router.post("/", commentController.get); //READ -> The function to get the medicine id, but the http body exists, so use post
router.get("/:medicineId", commentController.getComment); //READ
router.patch("/", verifyAccessToken, commentController.patch); //UPDATE
router.delete("/:medicineId/:medicineId/:commentId", verifyAccessToken, commentController.del); //DELETE

// Like
router.post("/:medicineId/like", verifyAccessToken, commentController.likePost); //CREATE
router.delete("/:medicineId/like/:commentId", verifyAccessToken, commentController.likeDelete); //DELETE
module.exports = router;