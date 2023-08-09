const multer = require("multer");
const { nanoid } = require("nanoid");

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, "./imgs"); // destination to store file
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + nanoid()); // 'Current time + random string'
    },
});

exports.upload = multer({ storage: storage });