"use strict";

module.exports = (sequelize, DataTypes) => {
    return sequelize.define('Comment', {
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
        MEDICINEID: {
            field: 'MEDICINE_ID',
            type: DataTypes.INTEGER,
            allowNull: false
        },
        SUBORDINATION: {
            field: 'SUBORDINATION',
            type: DataTypes.INTEGER,
            allowNull: false,
            defaultValue: 0
        },
        CONTENT: {
            field: 'CONTENT',
            type: DataTypes.STRING,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_COMMENT',
        timestamps: true,
        hooks: {
            beforeCreate: async (comment, options) => { // sequence to comment id mapping
                const result = await sequelize.query('SELECT COMMENT_ID_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                comment.ID = result[0].ID; // comment 객체의 ID와 DB_comment의 ID 매핑
            },
        }
    });
}