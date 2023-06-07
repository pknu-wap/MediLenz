"use strict";

const { User } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");
const { createAccessToken, createRefreshToken } = require("../config/jwt");

// sign-in response format
const tokenResponseFormat = (message, userId = null, nickname = null, email = null, access_token = null, refresh_token = null) => {
   let struct = {};
   struct.message = message;
    if (userId) struct.userId = userId;
    if (nickname) struct.nickname = nickname;
    if (email) struct.email = email;
    if (access_token) struct.access_token = access_token;
    if (refresh_token) struct.refresh_token = refresh_token;
    return struct;
}
const _getUserNickname = async (userId) => { //NOT FOR EXPORT
    try {
        const user = await User.findOne({
            attributes: ["NICKNAME"],
            where: {
                ID: userId
            }
        });
        return user.NICKNAME;
    } catch (err) {
        return null;
    }
}
// sign-in
const login = async (email, password) => {
    const user = await User.findOne({
        attributes: ["ID", "PASSWORD", "NICKNAME", "EMAIL"],
        where: {
            EMAIL: email
        }
    });
    if (!user) { // if user not exists
        return responseFormat(404, tokenResponseFormat(responseMsg.SIGNIN_USER_NOT_FOUND));
    }
    if (password != user.PASSWORD) { // password mismatch
        return responseFormat(401, tokenResponseFormat(responseMsg.SIGNIN_PASSWORD_MISMATCH));
    }

    const message = responseMsg.SIGNIN_SUCCESS; // generate response message
    const accessToken = createAccessToken(user.ID); // generate access token
    const refreshToken = createRefreshToken(user.ID); // generate refresh token

    return responseFormat(200, tokenResponseFormat(message, user.ID, user.NICKNAME, user.EMAIL, accessToken, refreshToken));
}

// duplicate check
const duplicateCheck = async (parameter) => {
    if (await User.findOne({ // if user exists
        where: parameter
    })) {
        return true;
    }
    return false; // if user not exists
}

// get user info
const getUserInfo = async (user_id) => {
    try {
        // get user
        const user = (await User.findOne({
            attributes: ["ID", "EMAIL", "NICKNAME"],
            where: { ID: user_id }
        })).dataValues;
        return responseFormat(200, {
            userId: user.ID,
            email: user.EMAIL,
            nickname: user.NICKNAME,
            message: responseMsg.USER_GET_COMPLETE,
        }); // get user SUCCESS
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.USER_GET_FAIL });
    }
}

// create user
const createUser = async (email, password, nickname) => {
    // duplicate check
    let checkParam = "EMAIL"
    if (await duplicateCheck({ [checkParam]: email })) { // email
        return responseFormat(409, tokenResponseFormat(responseMsg.DUPLICATE_PARAMETER(checkParam)));
    }
    checkParam = "NICKNAME";
    if (await duplicateCheck({ [checkParam]: nickname })) { // nickname
        return responseFormat(409, tokenResponseFormat(responseMsg.DUPLICATE_PARAMETER(checkParam)));
    }

    try {
        const user = await User.create({ // insert new User into DB
            EMAIL: email,
            PASSWORD: password,
            NICKNAME: nickname
        });

        const message = responseMsg.SIGNUP_SUCCESS; // generate response message
        const accessToken = createAccessToken(user.ID); // generate access token
        const refreshToken = createRefreshToken(user.ID); // generate refresh token

        return responseFormat(201, tokenResponseFormat(message, user.ID, user.NICKNAME, user.EMAIL, accessToken, refreshToken));

    }
    catch (err) {
        console.log(err);
        return responseFormat(500, tokenResponseFormat(responseMsg.SIGNUP_INTERNAL_SERVER_ERROR));
    }
}

// reissue token
const reissueToken = (userId) => {
    const message = responseMsg.REISSUE_SUCCESS;
    const accessToken = createAccessToken(userId); // generate access token
    const refreshToken = createRefreshToken(userId); // generate refresh token
    return responseFormat(200, tokenResponseFormat(message, null, null, null, accessToken, refreshToken));
}

// update user information
const updateUserInfo = async (user_id, new_nickname, new_password) => {
    let updateCondition, checkParam;
    if (new_nickname && !new_password) { // update user nickname
        checkParam = "NICKNAME";
        updateCondition = { [checkParam]: new_nickname };
        // nickname duplicate check
        if (await duplicateCheck(updateCondition)) {
            return responseFormat(409, responseMsg.DUPLICATE_PARAMETER(checkParam));
        }
    } else if (!new_nickname && new_password) { // update user password
        checkParam = "PASSWORD";
        updateCondition = { [checkParam]: new_password };
    }

    try {
        // update user information
        await User.update(
            updateCondition,
            { where: { ID: user_id } }
        );
        return responseFormat(200, { message: responseMsg.USER_UPDATE_COMPLETE(checkParam) });
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.USER_UPDATE_FAIL(checkParam) });
    }
}

// delete user
const deleteUser = async (user_id) => {
    try {
        // update email with destroyed_{user_id}, nickname with 알수없음_{user_id}
        const updated = await User.update(
            {
                EMAIL: `destroyed_${user_id}`,
                NICKNAME: `알수없음_${user_id}`
            },
            { where: { ID: user_id } }
        );
        return responseFormat(200, {
            message: responseMsg.USER_DELETE_COMPLETE,
        }); // delete SUCCESS
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.USER_DELETE_FAIL });
    }
}

module.exports = {
    _getUserNickname,
    login,
    createUser,
    reissueToken,
    updateUserInfo,
    deleteUser,
    getUserInfo
}