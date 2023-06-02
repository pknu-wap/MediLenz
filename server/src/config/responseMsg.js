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
    SIGNUP_INTERNAL_SERVER_ERROR: "DB Error",

    // Comment
    COMMENT_WRITE_COMPLETE: "comment write complete",

    // Medicine
    MEDICINE_ID_FOUND: "medicine id found",

    // Like
    LIKE_ADD_COMPLETE: "like add complete",
    LIKE_REMOVE_COMPLETE: "like remove complete",
    LIKE_ADD_FAIL: "like add fail",
    LIKE_REMOVE_FAIL: "like remove fail",
    LIKE_FOUND: "like found",
    LIKE_NOT_FOUND: "like not found",
}