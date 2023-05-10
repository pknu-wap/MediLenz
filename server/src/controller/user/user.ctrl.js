"use strict";

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
            res.status(400).send(responseMsg.SIGNIN_BAD_REQUEST);
        }
        const result = await login(email, password);
        return res.status(result.code).send(result.response);
    },
    // Sign-up
    // [POST] /user/register
    register: async (req, res) => {
        const { email, password, nickname } = req.body;
        if (!(email && password && nickname)) { // parameter check
            res.status(400).send(responseMsg.SIGNUP_BAD_REQUEST);
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