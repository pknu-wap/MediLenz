"use strict";

module.exports = (sequelize, DataTypes) => {
    return sequelize.define('Like', {
        ID: {
            field: 'ID',
            type: DataTypes.INTEGER,
            primaryKey: true,
            allowNull: false,
            defaultValue: 0,
        },
        USERID: {
            field: 'USER_ID',
            type: DataTypes.INTEGER,
            allowNull: false
        },
        COMMENTID: {
            field: 'COMMENT_ID',
            type: DataTypes.INTEGER,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_LIKE',
        hooks: {
            beforeCreate: async (data, options) => { // sequence to data id mapping
                const result = await sequelize.query('SELECT LIKE_ID_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                data.ID = result[0].ID; // data 객체의 ID와 DB_LIKE의 ID 매핑
            },
        }
    });
}