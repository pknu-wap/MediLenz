"use strict";

const router = require("express").Router();
const commentController = require("../../../controller/comment");
const { verifyAccessToken } = require("../../../config/jwt");

// Comment
router.post("/writeTest", verifyAccessToken, commentController.writeTest); //CREATE
router.get("/", commentController.get); //READ
router.get("/:medicineId", commentController.getComment); //READ
router.patch("/", verifyAccessToken, commentController.patch); //UPDATE
router.delete("/", verifyAccessToken, commentController.del); //DELETE

// Like
router.post("/:medicineId/like", verifyAccessToken, commentController.likePost); //CREATE
router.delete("/:medicineId/like", verifyAccessToken, commentController.likeDelete); //DELETE
module.exports = router;