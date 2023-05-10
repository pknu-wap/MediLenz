"use strict";

module.exports = (sequelize, DataTypes) => {
    return sequelize.define('Medicine', {
        ITEM_SEQ: {
            field: 'ITEM_SEQ',
            type: DataTypes.STRING,
            primaryKey: true,
            allowNull: false,
            defaultValue: 0,
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
        },
        INGREDIENT_NAME: {
            field: 'INGREDIENT_NAME',
            type: DataTypes.STRING,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_MEDICINE'
    });
}