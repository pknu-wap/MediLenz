"use strict";

module.exports = {
    // Success
    SIGNIN_SUCCESS: { message: "login success" },
    SIGNUP_SUCCESS: { message: "new user created" },
    REISSUE_SUCCESS: { message: "reissued tokens" },

    // Request error
    JWT_INVALID_FORMAT: { message: "token is empty or sent format is wrong" },
    SIGNIN_BAD_REQUEST: { message: "paramter must include 'email(string), password(string)'" },
    SIGNIN_USER_NOT_FOUND: { message: "user not found" },
    SIGNIN_PASSWORD_MISMATCH: { message: "password mismatch" },
    SIGNUP_BAD_REQUEST: { message: "paramter must include 'email(string), password(string), nickname(string)'" },

    // Response error
    SIGNUP_DUPLICATE_PARAMETER: (parameter) => {
        return { message: `the ${parameter} is already in use` }
    },
    SIGNUP_INTERNAL_SERVER_ERROR: { message: "DB Error" }

}