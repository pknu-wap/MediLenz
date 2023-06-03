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
    SIGNUP_INTERNAL_SERVER_ERROR: "server error",

    // Comment
    COMMENT_GET_COMPLETE: "comment get complete",
    COMMENT_WRITE_COMPLETE: "comment write complete",
    COMMENT_WRITE_FAIL: "comment write fail",
    COMMENT_NOT_FOUND: "comment not found",
    COMMENT_DELETE_COMPLETE: "comment delete complete",
    COMMENT_DELETE_FAIL: "comment delete fail",
    COMMENT_EDIT_COMPLETE: "comment edit complete",
    COMMENT_EDIT_FAIL: "comment edit fail",
    // Medicine
    MEDICINE_ID_FOUND: "medicine id found",
    // Medicine_Favorite
    MEDICINE_FAVORITE_LIST_GET_COMPLETE: "favorite medicine list get complete",
    MEDICINE_FAVORITE_LIST_GET_FAIL: "favorite medicine list get fail",
    MEDICINE_FAVORITE_GET_COMPLETE: "favorite medicine get complete",
    MEDICINE_FAVORITE_GET_FAIL: "favorite medicine get fail",
    MEDICINE_FAVORITE_ADD_COMPLETE: "favorite medicine add complete",
    MEDICINE_FAVORITE_ADD_FAIL: "comment write fail",

    // Like
    LIKE_ADD_COMPLETE: "like add complete",
    LIKE_REMOVE_COMPLETE: "like remove complete",
    LIKE_ADD_FAIL: "like add fail",
    LIKE_REMOVE_FAIL: "like remove fail",
    LIKE_FOUND: "like found",
    LIKE_NOT_FOUND: "like not found",
}