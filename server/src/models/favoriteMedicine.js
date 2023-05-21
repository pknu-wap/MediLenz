"use strict";

module.exports = function (sequelize, DataTypes) {
    const FavoriteMedicine = sequelize.define('FavoriteMedicine', {
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
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_FAVORITE_MEDICINE',
        timestamps: true,
        hooks: {
            beforeCreate: async (favoriteMedicine, options) => { // sequence to data id mapping
                const result = await sequelize.query('SELECT FAVORITE_MEDICINE_ID_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                favoriteMedicine.ID = result[0].ID; // data 객체의 ID와 DB_FAVORITE_MEDICINE의 ID 매핑
            },
        }
    });

    FavoriteMedicine.associate = function (models) {
        FavoriteMedicine.belongsTo(models.User, {foreignKey: 'USERID', sourceKey: 'ID'});
        FavoriteMedicine.belongsTo(models.Medicine, {foreignKey: 'MEDICINEID', sourceKey: 'ID'});
    }
    return FavoriteMedicine;
}
