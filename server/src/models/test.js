module.exports = (sequelize, DataTypes) => {
    const User = sequelize.define('DB_TEST', {
        UserId: {
            field: 'TEST',
            type: DataTypes.STRING(32),
            primaryKey: true,
            allowNull: false,
            defaultValue: ''
        }
    }, {
        underscored: true,
        freezeTableName: true,
        tableName: 'DB_TEST'
    });
    return User;
}