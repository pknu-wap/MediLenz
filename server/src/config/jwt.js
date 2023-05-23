"use strict";

const jwt = require('jsonwebtoken');
const { responseFormat } = require("./response")
const responseMsg = require("./responseMsg");
const AUTHORIZATION_HEADER = 'authorization'

// generate access token
const createAccessToken = (userId) => {
    return jwt.sign({ userId }, process.env.JWT_ACCESS_TOKEN_SECRETKEY, {
        expiresIn: "30m"
    })
}

// generate refresh token
const createRefreshToken = (userId) => {
    return jwt.sign({ userId }, process.env.JWT_REFRESH_TOKEN_SECRETKEY, {
        expiresIn: "180d"
    })
}

// resolve token
const resolveToken = (req) => {
    console.log(req.headers)
    const token = req.headers[AUTHORIZATION_HEADER] // extract token from http header
    console.log(token)
    if (token && token.startsWith("Bearer ")) { // if token exist
        return token.substring(7); // return pure token value
    }
    return null;
}

// verifying access token 
const verifyAccessToken = (req, res, next) => {
    const token = resolveToken(req); // parsing token
    if (!token) { // if token does not exist
        const result = responseFormat(401, responseMsg.JWT_INVALID_FORMAT); // 401 unauthorized
        return res.status(result.code).send(result.response);
    }
    jwt.verify(token, process.env.JWT_ACCESS_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            const result = responseFormat(401, { message: err.message }); // 401 unauthorized
            return res.status(result.code).send(result.response);
        }
        req.verifiedToken = data;
        next();
    })
}

// verifying refresh token 
const verifyRefreshToken = (req, res, next) => {
    const token = resolveToken(req); // parsing token
    if (!token) { // if token does not exist
        const result = responseFormat(401, responseMsg.JWT_INVALID_FORMAT); // 401 unauthorized
        return res.status(result.code).send(result.response);
    }
    jwt.verify(token, process.env.JWT_REFRESH_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            const result = responseFormat(401, { message: err.message }); // 401 unauthorized
            return res.status(result.code).send(result.response);
        }
        req.verifiedToken = data;
        next();
    })
}

module.exports = {
    createAccessToken,
    createRefreshToken,
    verifyAccessToken,
    verifyRefreshToken
}