module.exports = (sequelize, DataTypes) => {
    return sequelize.define('User', {
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
        hooks: {
            beforeCreate: async (user, options) => {
                const result = await sequelize.query('SELECT USER_ID_SEQ.NEXTVAL AS ID FROM DUAL', {
                    type: sequelize.QueryTypes.SELECT
                });
                // User 객체의 ID와 DB_USER의 ID 매핑
                user.UserId = result[0].ID;
            },
        }
    });
}