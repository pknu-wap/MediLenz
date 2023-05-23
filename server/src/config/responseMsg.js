"use strict";

module.exports = {
    // Success
    SIGNIN_SUCCESS: "login success",
    SIGNUP_SUCCESS: "new user created",
    REISSUE_SUCCESS: "reissued tokens",

    // Request error
    JWT_INVALID_FORMAT: "token is empty or sent format is wrong",
    SIGNIN_BAD_REQUEST: "paramter must include 'email(string), password(string)'",
    SIGNIN_USER_NOT_FOUND: "user not found",
    SIGNIN_PASSWORD_MISMATCH: "password mismatch",
    SIGNUP_BAD_REQUEST: "paramter must include 'email(string), password(string), nickname(string)'",

    // Response error
    SIGNUP_DUPLICATE_PARAMETER: (parameter) => {
        return `the ${parameter} is already in use`
    },
    SIGNUP_INTERNAL_SERVER_ERROR: "DB Error"

}