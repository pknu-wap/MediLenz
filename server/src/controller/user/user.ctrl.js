"use strict";

const { responseFormat } = require("../../config/response")
const responseMsg = require("../../config/responseMsg");
const { login, createUser, reissueToken, updateUserInfo, deleteUser, getUserInfo } = require("../../service/userService");
const { getCommentListFromUserId } = require("../../service/commentService");
// GET
const output = {
    // GET user info
    // [GET] /user
    getUserInfo: async (req, res) => {
        const { userId } = req.verifiedToken;
        const result = await getUserInfo(userId); // get user info
        return res.status(result.code).send(result.response);
    },
    getCommentListFromUserId: async (req, res) => {
        const { userId } = req.verifiedToken;
        const result = await getCommentListFromUserId(userId); // get user info
        return res.status(result.code).send(result.response);
    }
}

// POST
const process = {
    // Sign-in
    // [POST] /user/login 
    login: async (req, res) => {
        const { email, password } = req.body;
        if (!(email && password)) { // parameter check
            const result = responseFormat(400, responseMsg.SIGNIN_BAD_REQUEST);
            return res.status(result.code).send(result.response);
        }
        const result = await login(email, password); // access to account
        return res.status(result.code).send(result.response);
    },
    // Sign-up
    // [POST] /user/register
    register: async (req, res) => {
        const { email, password, nickname } = req.body;
        if (!(email && password && nickname)) { // parameter check
            const result = responseFormat(400, responseMsg.SIGNUP_BAD_REQUEST);
            return res.status(result.code).send(result.response);
        }
        const result = await createUser(email, password, nickname); // create user
        return res.status(result.code).send(result.response);
    },
    // Reissuing token
    // [POST] /user/reissue
    reissue: (req, res) => {
        const { userId } = req.verifiedToken;
        const result = reissueToken(userId); // reissuing token
        console.log(result);
        return res.status(result.code).send(result.response);
    }
}

// PUT
const edit = {
    // Edit user nickname/password
    // [PATCH] /user
    patchUserInfo: async (req, res) => {
        const { userId } = req.verifiedToken;
        const { newNickname, newPassword } = req.body;
        const result = await updateUserInfo(userId, newNickname, newPassword); // update user nickname or password
        return res.status(result.code).send(result.response);
    }
}

// DELETE
const eliminate = {
    // Delete user
    // [DELETE] /user
    deleteUser: async (req, res) => {
        const { userId } = req.verifiedToken;
        const result = await deleteUser(userId); // delete user
        return res.status(result.code).send(result.response);
    }
}

module.exports = {
    output,
    process,
    edit,
    eliminate,
};