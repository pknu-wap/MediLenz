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
        return responseFormat(409, tokenResponseFormat(responseMsg.SIGNUP_DUPLICATE_PARAMETER(checkParam)));
    }
    checkParam = "NICKNAME";
    if (await duplicateCheck({ [checkParam]: nickname })) { // nickname
        return responseFormat(409, tokenResponseFormat(responseMsg.SIGNUP_DUPLICATE_PARAMETER(checkParam)));
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

module.exports = {
    _getUserNickname,
    login,
    createUser,
    reissueToken
}