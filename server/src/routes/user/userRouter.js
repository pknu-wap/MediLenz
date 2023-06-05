"use strict";

const userCtrl = require('../../controller/user/user.ctrl');
const { verifyRefreshToken, verifyAccessToken } = require("../../config/jwt");
const router = require("express").Router();

/**
 * @swagger
 * /user/login:
 *   post:
 *    summary: "로그인"
 *    description: "사용자의 email과 password을 입력받아, access token과 refresh token을 발급합니다."
 *    tags: [Users]
 *    requestBody:
 *      description: 유저 정보
 *      required: true
 *      content:
 *        application/x-www-form-urlencoded:
 *          schema:
 *            type: object
 *            properties:
 *              email:
 *                type: string
 *                description: "유저 이메일"
 *              password:
 *                type: string
 *                description: "유저 비밀번호"
 *    responses:
 *      "200":
 *        description: 로그인 성공
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "login success"
 *                accessToken:
 *                  type: string
 *                  example: "Access token value"
 *                refreshToken:
 *                  type: string
 *                  example: "Refresh token value"
 *      "401":
 *        description: 비밀번호 입력 오류
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "password mismatch"
 *                accessToken:
 *                  type: string
 *                  example: null
 *                refreshToken:
 *                  type: string
 *                  example: null
 *      "404":
 *        description: 해당 유저가 존재하지 않음
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "user not found"
 *                accessToken:
 *                  type: string
 *                  example: null
 *                refreshToken:
 *                  type: string
 *                  example: null
 */
router.post("/login", userCtrl.process.login); // sign-in

router.post("/reissue", verifyRefreshToken, userCtrl.process.reissue); // reissuing token

/**
 * @swagger
 * /user/register:
 *   post:
 *    summary: "회원가입"
 *    description: "사용자로부터 email, nickname, password를 입력받아 DB에 저장하고, access token과 refresh token을 발급합니다."
 *    tags: [Users]
 *    requestBody:
 *      description: 유저 정보
 *      required: true
 *      content:
 *        application/x-www-form-urlencoded:
 *          schema:
 *            type: object
 *            properties:
 *              email:
 *                type: string
 *                description: "유저 이메일"
 *              password:
 *                type: string
 *                description: "유저 비밀번호"
 *              nickname:
 *                type: string
 *                description: "유저 닉네임"
 *    responses:
 *      "201":
 *        description: 회원가입 성공
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "new user created"
 *                accessToken:
 *                  type: string
 *                  example: "Access token value"
 *                refreshToken:
 *                  type: string
 *                  example: "Refresh token value"
 *      "409":
 *        description: 이메일 or 닉네임 중복
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "the email is already in use"
 *                accessToken:
 *                  type: string
 *                  example: null
 *                refreshToken:
 *                  type: string
 *                  example: null
 *      "500":
 *        description: 서버 오류
 *        content:
 *          application/json:
 *            schema:
 *              type: object
 *              properties:
 *                message:
 *                  type: string
 *                  example: "server error"
 *                accessToken:
 *                  type: string
 *                  example: null
 *                refreshToken:
 *                  type: string
 *                  example: null
 */
router.post("/register", userCtrl.process.register); // sign-up
router.patch("/", verifyAccessToken, userCtrl.edit.patchUserNickname) // edit user nickname

module.exports = router;