"use strict";

// 모듈
const express = require("express");
const app = express();
const bodyParser = require('body-parser');
const dotenv = require("dotenv");
dotenv.config();

// 라우팅
const home = require("./src/routes/home");

// 미들웨어
app.use(bodyParser.json()); //use body-parser
app.use("/", home);

module.exports = app;