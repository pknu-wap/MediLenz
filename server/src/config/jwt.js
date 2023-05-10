"use strict";

const jwt = require('jsonwebtoken');
const responseMsg = require("./responseMsg");
const AUTHORIZATION_HEADER = 'Authorization'

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
    const token = req.header[AUTHORIZATION_HEADER] // extract token from http header
    if (token && token.startsWith("Bearer ")) { // if token exist
        return token.substring(7); // return pure token value
    }
    return null;
}

// verifying access token 
const verifyAccessToken = (req, res, next) => {
    const token = resolveToken(req); // parsing token
    if (token) { // if token does not exist
        return res.sendStatus(401).send(responseMsg.JWT_INVALID_FORMAT); // 401 unauthorized
    }
    jwt.verify(token, process.env.JWT_ACCESS_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            res.sendStatus(401).send(err); // 401 unauthorized
        }
        req.verifiedToken = data;
        next();
    })
}

// verifying refresh token 
const verifyRefreshToken = (req, res, next) => {
    const token = resolveToken(req); // parsing token
    if (token) { // if token does not exist
        return res.sendStatus(401).send(responseMsg.JWT_INVALID_FORMAT); // 401 unauthorized
    }
    jwt.verify(token, process.env.JWT_REFRESH_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            res.sendStatus(401).send(err); // 401 unauthorized
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