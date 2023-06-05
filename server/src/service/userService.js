"use strict";

const { User } = require("../models/index");
const { responseFormat } = require("../config/response");
const responseMsg = require("../config/responseMsg");
const { createAccessToken, createRefreshToken } = require("../config/jwt");

// sign-in response format
const tokenResponseFormat = (message, access_token = null, refresh_token = null) => {
    return {
        message,
        access_token,
        refresh_token
    }
}

// sign-in
const login = async (email, password) => {
    const userInfo = await User.findOne({
        attributes: ["ID", "PASSWORD"],
        where: {
            EMAIL: email
        }
    });
    if (!userInfo) { // if user not exists
        return responseFormat(404, tokenResponseFormat(responseMsg.SIGNIN_USER_NOT_FOUND));
    }
    if (password != userInfo.PASSWORD) { // password mismatch
        return responseFormat(401, tokenResponseFormat(responseMsg.SIGNIN_PASSWORD_MISMATCH));
    }

    const message = responseMsg.SIGNIN_SUCCESS; // generate response message
    const accessToken = createAccessToken(userInfo.ID); // generate access token
    const refreshToken = createRefreshToken(userInfo.ID); // generate refresh token

    return responseFormat(200, tokenResponseFormat(message, accessToken, refreshToken));
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

        return responseFormat(201, tokenResponseFormat(message, accessToken, refreshToken));

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
    return responseFormat(200, tokenResponseFormat(message, accessToken, refreshToken));
}

// update user nickname
const updateUserNickname = async (user_id, new_nickname) => {
    // nickname duplicate check
    if (await duplicateCheck({ NICKNAME: new_nickname })) {
        return responseFormat(409, responseMsg.DUPLICATE_PARAMETER("NICKNAME"));
    }
    try {
        // update user nickname
        await User.update(
            { NICKNAME: new_nickname },
            { where: { ID: user_id } }
        );
        return responseFormat(200, { message: responseMsg.USER_UPDATE_NICKNAME_COMPLETE });
    }
    catch (err) {
        console.log(err);
        return responseFormat(500, { message: responseMsg.USER_UPDATE_NICKNAME_FAIL });
    }
}

module.exports = {
    login,
    createUser,
    reissueToken,
    updateUserNickname
}