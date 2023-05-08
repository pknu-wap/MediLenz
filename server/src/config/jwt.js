const jwt = require('jsonwebtoken');

const AUTHORIZATION_HEADER = 'Authorization'

// generate access token
const createAccessToken = (email) => {
    return jwt.sign({ email }, process.env.JWT_ACCESS_TOKEN_SECRETKEY, {
        expiresIn: "30m"
    })
}

// generate refresh token
const createRefreshToken = (email) => {
    return jwt.sign({ email }, process.env.JWT_REFRESH_TOKEN_SECRETKEY, {
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
        console.log("token is empty or sent format is wrong")
        return res.sendStatus(401); // 401 unauthorized
    }
    jwt.verify(token, process.env.JWT_ACCESS_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            res.sendStatus(403); // 403 forbidden
        }
        req.verifiedToken = data;
        next();
    })
}

// verifying refresh token 
const verifyRefreshToken = (req, res, next) => {
    const token = resolveToken(req); // parsing token
    if (token) { // if token does not exist
        console.log("token is empty or sent format is wrong")
        return res.sendStatus(401); // 401 unauthorized
    }
    jwt.verify(token, process.env.JWT_REFRESH_TOKEN_SECRETKEY, (err, data) => { // verifing token
        if (err) {
            console.log(err)
            res.sendStatus(403); // 403 forbidden
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