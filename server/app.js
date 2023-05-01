"use strict";

// 모듈
const express = require("express");
const app = express();

// 라우팅
const home = require("./src/routes/home");

// 미들웨어
app.use("/", home); 

module.exports = app;