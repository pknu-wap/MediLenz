"use strict";

const { responseFormat } = require("../../config/response")
const responseMsg = require("../../config/responseMsg");
const { login, createUser } = require("../../service/userService");

// GET
const output = {
    //
}

// POST
const process = {
    // Sign-in
    // [POST] /user/login 
    login: async (req, res) => {
        const { email, password } = req.body;
        if (!(email, password)) { // parameter check
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
    }
}

// PUT
const edit = {

}

// DELETE
const eliminate = {

}

module.exports = {
    output,
    process,
    edit,
    eliminate,
};