"use strict";

const app = require("../app");
const models = require('../src/models/index.js');
const PORT = process.env.PORT || 3000; // 포트

const { auto } = require("../src/config/sequelizeAuto")

const oracledb = require("oracledb")


app.listen(PORT, async () => {
  if (process.env.NODE_ENV == 'development') { // 현재 개발 환경이라면
    oracledb.initOracleClient({ libDir: process.env.DB_ORACLEHOME }); // 개발 머신에 따라 oracle client 경로 수동 설정
  }

  try {
    await models.sequelize.sync();
  } catch (err) {
    console.log('DB 연결 중 오류 발생: ', err);
    process.exit();
  }

  // Find all users
  const users = await models.User.findAll();
  console.log(users.every(user => user instanceof models.User)); // true
  console.log("All users:", JSON.stringify(users, null, 2));

  // // model auto generation test
  // auto.run((err) => {
  //   if (err) throw err;
  //   console.log(auto.tables); // 생성된 모델 확인
  // });

  console.log(`Server running on port ${PORT}`);
});