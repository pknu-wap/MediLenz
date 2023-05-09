"use strict";

module.exports = {
    // Success
    SIGNUP_SUCCESS: { message: "new user created" },

    // Request error
    SIGNUP_BAD_REQUEST: { message: "paramter must include 'email(string), password(string), nickname(string)'" },

    // Response error
    SIGNUP_DUPLICATE_PARAMETER: (parameter) => {
        return { message: `the ${parameter} is already in use` }
    },
    SIGNUP_INTERNAL_SERVER_ERROR: { message: "DB Error" }

}