const SequelizeAuto = require('sequelize-auto');
const auto = new SequelizeAuto(process.env.DB_DATABASE, process.env.DB_USER, process.env.DB_PASSWORD, {
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    directory: "../models", // 모델 파일이 생성될 디렉토리 경로
    dialect: "oracle"
}
);

module.exports = { auto };