"use strict";

module.exports = function (sequelize, DataTypes) {
    const Medicine = sequelize.define('Medicine', {
        ID: {
            field: 'ID',
            type: DataTypes.INTEGER,
            primaryKey: true,
            allowNull: false,
            defaultValue: 0
        },
        ITEM_SEQ: {
            field: 'ITEM_SEQ',
            type: DataTypes.STRING,
            allowNull: false,
        },
        ITEM_NAME: {
            field: 'ITEM_NAME',
            type: DataTypes.STRING,
            allowNull: false
        },
        ITEM_INGR_NAME: {
            field: 'ITEM_INGR_NAME',
            type: DataTypes.STRING,
            allowNull: false
        },
        PRDUCT_TYPE: {
            field: 'PRDUCT_TYPE',
            type: DataTypes.STRING,
            allowNull: false
        },
        ENTP_NAME: {
            field: 'ENTP_NAME',
            type: DataTypes.STRING,
            allowNull: false
        },
        SPCLTY_PBLC: {
            field: 'SPCLTY_PBLC',
            type: DataTypes.STRING,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_MEDICINE',
        hooks: {
            beforeCreate: async (medicine, options) => { // sequence to user id mapping
                const result = await sequelize.query('SELECT MEDICINE_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                medicine.ID = result[0].ID; // User 객체의 ID와 DB_USER의 ID 매핑
            },
        }
    });
    Medicine.associate = function (models) {
        Medicine.hasMany(models.FavoriteMedicine, {foreignKey: 'MEDICINEID', targetKey: 'ID'});
        Medicine.hasMany(models.Comment, {foreignKey: 'MEDICINEID', targetKey: 'ID'});
    }
    return Medicine;
}