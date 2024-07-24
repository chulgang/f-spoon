package config;

import java.sql.Connection;
import java.sql.SQLException;

public class DbConnectionThreadLocal {
    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> sqlErrorThreadLocal = ThreadLocal.withInitial(() -> false);

    public static void initialize() {
        try {
            Connection connection = DBUtil.getDataSource().getConnection();
            connectionThreadLocal.set(connection);

            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    public static Connection getConnection() {
        return connectionThreadLocal.get();
    }

    public static void setSqlError(boolean sqlError) {
        sqlErrorThreadLocal.set(sqlError);
    }

    public static boolean getSqlError() {
        return sqlErrorThreadLocal.get();
    }

    public static void reset() {
        try {
            Connection connection = connectionThreadLocal.get();
            if (connection != null) {
                if (getSqlError()) {
                    connection.rollback();
                } else {
                    connection.commit();
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionThreadLocal.remove();
            sqlErrorThreadLocal.remove();
        }
    }
}
