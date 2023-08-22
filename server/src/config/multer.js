const multer = require("multer");
const path = require("path");
const { nanoid } = require("nanoid");
const { imgsDir } = require("../config/staticDirLoc");

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, imgsDir); // destination to store file
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + nanoid() + path.extname(file.originalname)); // 'Current time + random string + file extension'
    },
});

exports.upload = multer({ storage: storage });
