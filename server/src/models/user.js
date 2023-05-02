module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define('User', {
        UserId: {
            field: 'ID',
            type: DataTypes.INTEGER,
            primaryKey: true,
            allowNull: false,
            defaultValue: 0,
        },
        EMAIL: {
            field: 'EMAIL',
            type: DataTypes.INTEGER,
            allowNull: false
        },
        Password: {
            field: 'PASSWORD',
            type: DataTypes.STRING,
            allowNull: false
        },
        NICKNAME: {
            field: 'NICKNAME',
            type: DataTypes.STRING,
            allowNull: false
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_USER',
        timestamps: true,
    });
    return User;
}