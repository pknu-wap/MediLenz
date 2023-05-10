"use strict";

module.exports = (sequelize, DataTypes) => {
    return sequelize.define('User', {
        USERID: {
            field: 'ID',
            type: DataTypes.INTEGER,
            primaryKey: true,
            allowNull: false,
            defaultValue: 0,
        },
        EMAIL: {
            field: 'EMAIL',
            type: DataTypes.STRING,
            allowNull: false,
            unique: true
        },
        PASSWORD: {
            field: 'PASSWORD',
            type: DataTypes.STRING,
            allowNull: false
        },
        NICKNAME: {
            field: 'NICKNAME',
            type: DataTypes.STRING,
            allowNull: false,
            unique: true
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_USER',
        timestamps: true,
        hooks: {
            beforeCreate: async (user, options) => { // sequence to user id mapping
                const result = await sequelize.query('SELECT USER_ID_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                user.USERID = result[0].ID; // User 객체의 ID와 DB_USER의 ID 매핑
            },
        }
    });
}