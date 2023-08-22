const swaggerUi = require("swagger-ui-express")
const swaggereJsdoc = require("swagger-jsdoc")

const options = {
    swaggerDefinition: {
        openapi: "3.0.0",
        info: {
            version: "1.0.0",
            title: "MediLenz",
            description:
                "MediLenz API 명세서입니다.",
        },
        servers: [
            {
                url: "http://localhost:" + process.env.PORT, // 요청 URL
            },
        ],
    },
    apis: ["./src/routes/home/*.js", "./src/routes/user/*.js"], // Swagger 파일 연동
}
const specs = swaggereJsdoc(options)

module.exports = { swaggerUi, specs }