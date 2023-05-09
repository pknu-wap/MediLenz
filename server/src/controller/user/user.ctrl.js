"use strict";

const responseMsg = require("../../config/responseMsg");
const { createUser } = require("../../service/userService");

// GET
const output = {

}

// POST
const process = {
    // Sign-up
    // [POST] /user/register
    register: async (req, res) => {
        const { email, password, nickname } = req.body;
        if (!(email && password && nickname)) {
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