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


module.exports = {
    createAccessToken,
    createRefreshToken,

}