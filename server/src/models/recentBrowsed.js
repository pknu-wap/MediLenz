"use strict";

module.exports = function (sequelize, DataTypes) {
    const RecentBrowsed = sequelize.define('RecentBrowsed', {
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
        QUERY: {
            field: 'QUERY',
            type: DataTypes.STRING,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_RECENT_BROWSED',
        timestamps: true,
        hooks: {
            beforeCreate: async (recentBrowsed, options) => { // sequence to data id mapping
                const result = await sequelize.query('SELECT RECENT_BROWSED_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                recentBrowsed.ID = result[0].ID; // data 객체의 ID와 DB_RECENT_BROWSED의 ID 매핑
            },
        }
    });

    RecentBrowsed.associate = function (models) {
        RecentBrowsed.belongsTo(models.User, {foreignKey: 'USERID', sourceKey: 'ID'});
    }
    return RecentBrowsed;
}