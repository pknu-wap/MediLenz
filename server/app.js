"use strict";

// 모듈
const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const dotenv = require("dotenv");
dotenv.config();
const { swaggerUi, specs } = require("./swagger/swagger"); // swagger
const path = require("path");
const { imgsDir } = require("./src/config/staticDirLoc"); // path to save uploaded files

// 라우팅
const home = require("./src/routes/home");

// 미들웨어
// app.use(bodyParser.json());
app.use(express.json()); // 내장 body-parser
app.use(express.urlencoded({ extended: true }));

// set static folder
app.use("/imgs", express.static(imgsDir)); // folder destination

app.use("/", home);

app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(specs));

module.exports = app;
