"use strict";

module.exports = {
    response: (code, message) => {
        return {
            code,
            message
        }
    }
}