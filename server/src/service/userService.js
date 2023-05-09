"use strict";

const { User } = require("../models/index");
const responseMsg = require("../config/responseMsg");
const { createAccessToken, createRefreshToken } = require("../config/jwt");

// duplicate check
const duplicateCheck = async (parameter) => {
    if ((await User.findAll({ // if user not exists
        where: parameter
    })).length == 0) {
        return false;
    }
    return true; // if user exists
}

// create user
const createUser = async (email, password, nickname) => {
    // duplicate check
    let checkParam = "EMAIL"
    if (await duplicateCheck({ [checkParam]: email })) { // email
        return {
            code: 409,
            response: responseMsg.SIGNUP_DUPLICATE_PARAMETER(checkParam)
        };
    }
    checkParam = "NICKNAME";
    if (await duplicateCheck({ [checkParam]: nickname })) { // nickname
        return {
            code: 409,
            response: responseMsg.SIGNUP_DUPLICATE_PARAMETER(checkParam)
        };
    }

    try {
        const user = await models.User.create({ // insert new User into DB
            EMAIL: email,
            Password: password,
            NICKNAME: nickname
        });
        const response = responseMsg.SIGNUP_SUCCESS;
        response.accessToken = createAccessToken(user.EMAIL, user.NICKNAME); // generate access token
        response.refreshToken = createRefreshToken(user.EMAIL, user.NICKNAME); // generate refresh token

        return {
            code: 201,
            response
        };
    }
    catch (err) {
        console.log(err);
        return {
            code: 500,
            response: responseMsg.SIGNUP_INTERNAL_SERVER_ERROR
        };
    }
}

module.exports = {
    createUser
}