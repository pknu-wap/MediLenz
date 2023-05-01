const development = {
   dialect: 'oracle',
   host: process.env.DB_HOST,
   port: process.env.DB_PORT,
   username: process.env.DB_USER,
   password: process.env.DB_PASSWORD,
   database: process.env.DB_DATABASE
}

const production = {
   dialect: 'oracle',
   host: process.env.DB_HOST,
   port: process.env.DB_PORT,
   username: process.env.DB_USER,
   password: process.env.DB_PASSWORD,
   database: process.env.DB_DATABASE
}

const test = {
   dialect: 'oracle',
   host: process.env.DB_HOST,
   port: process.env.DB_PORT,
   username: process.env.DB_USER,
   password: process.env.DB_PASSWORD,
   database: process.env.DB_DATABASE
}

module.exports = { development, production, test }